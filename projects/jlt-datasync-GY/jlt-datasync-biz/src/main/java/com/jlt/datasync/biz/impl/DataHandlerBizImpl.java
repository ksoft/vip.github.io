package com.jlt.datasync.biz.impl;

import com.jlt.common.annotation.JobLog;
import com.jlt.common.constant.Globals;
import com.jlt.common.enums.DataReadSource;
import com.jlt.common.enums.DataSchema;
import com.jlt.common.enums.JobType;
import com.jlt.common.enums.YesNo;
import com.jlt.common.util.DataReadHandlerUtils;
import com.jlt.common.util.SpringUtils;
import com.jlt.datasync.biz.DataHandlerBiz;
import com.jlt.datasync.biz.JltJobDetailBiz;
import com.jlt.datasync.domain.JltJobDetail;
import com.jlt.datasync.dto.DataTransferDto;
import com.jlt.datasync.dto.DataWriteDto;
import com.jlt.datasync.exthandler.IExtDataReadHandler;
import com.sinoservices.minima.common.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.Future;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Service
public class DataHandlerBizImpl implements DataHandlerBiz {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("saasJdbcTemplate")
    private JdbcTemplate saasJdbcTemplate;
    @Autowired
    @Qualifier("upmJdbcTemplate")
    private JdbcTemplate upmJdbcTemplate;
    @Autowired
    @Qualifier("pmsJdbcTemplate")
    private JdbcTemplate pmsJdbcTemplate;
    @Autowired
    private JltJobDetailBiz jltJobDetailBiz;
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    @Async("asyncTaskExecutor")
    @JobLog(jobTye = JobType.READ)
    public Future<DataTransferDto> readData(JltJobDetail jltJobDetail, DataTransferDto dataTransferDto, String lastExecSuccessTime,
            Date now) {
        JdbcTemplate jdbcTemplate = this.getJdbcTemplate(jltJobDetail);
        Map<String, List<Map<String, Object>>> dataMap;
        if (DataSchema.SAAS.getKey().equals(jltJobDetail.getSchemaName())) {
            dataMap = dataTransferDto.getSaasDataMap();
        } else if (DataSchema.UPM.getKey().equals(jltJobDetail.getSchemaName())) {
            dataMap = dataTransferDto.getUpmDataMap();
        } else {
            dataMap = dataTransferDto.getPmsDataMap();
        }
        try {
            List<Map<String, Object>> dataList=new ArrayList<>();
            if(DataReadSource.REDIS.getKey().equals(jltJobDetail.getDataReadSource())) {//从redis里查数据
                String redisKey =
                        jltJobDetail.getRedisKey().replaceAll(Globals.TENANT_ID_REPLACE_STR, jltJobDetail.getTenantId().toString());
                Long size = redisTemplate.opsForList().size(redisKey);
                logger.info("【" + redisKey + "】redis data size ==【" + size + "】");
                for(long i=0;i<size;i++){
                    Map<String, Object> map = (Map<String, Object>) redisTemplate.opsForList().rightPop(redisKey);
                    dataList.add(map);
                }
            } else if (DataReadSource.DB.getKey().equals(jltJobDetail.getDataReadSource())) {//从数据库查数据
                String sql = this.getReadSql(jltJobDetail, jltJobDetail.getTenantId(), lastExecSuccessTime, now);
                dataList = jdbcTemplate.queryForList(sql);
            } else if (DataReadSource.EXT.getKey().equals(jltJobDetail.getDataReadSource())) {//根据自定义实现类来查询
                String beanName = DataReadHandlerUtils.getBeanName(jltJobDetail.getTableName());
                IExtDataReadHandler dataReadHandler = (IExtDataReadHandler) SpringUtils.getBean(beanName);
                // 注入对应的类进行业务处理
                dataList = dataReadHandler.process(jltJobDetail, lastExecSuccessTime, now);
            }
            if (!CollectionUtils.isEmpty(dataList)) {
                dataMap.put(jltJobDetail.getTableName(), dataList);
            }
            jltJobDetail.setLastExecSuccessTime(now);
            jltJobDetail.setLastExecResult(YesNo.YES.getUpperKey());
            jltJobDetail.setMsg(null);
            jltJobDetailBiz.update(jltJobDetail);
        } catch (Exception e) {
            jltJobDetail.setLastExecResult(YesNo.NO.getUpperKey());
            jltJobDetail.setMsg(e.getMessage());
            jltJobDetailBiz.update(jltJobDetail);
        }
        if (DataSchema.SAAS.getKey().equals(jltJobDetail.getSchemaName())) {
            dataTransferDto.setSaasDataMap(dataMap);
        } else if (DataSchema.UPM.getKey().equals(jltJobDetail.getSchemaName())) {
            dataTransferDto.setUpmDataMap(dataMap);
        } else {
            dataTransferDto.setPmsDataMap(dataMap);
        }
        return new AsyncResult<>(dataTransferDto);
    }

    @Override
    @Async("asyncTaskExecutor")
    @JobLog(jobTye = JobType.WRITE)
    public Future<DataWriteDto> writeData(JltJobDetail jltJobDetail, Map<String, List<Map<String, Object>>> dataMap, Date now,
            DataWriteDto dataWriteDto) {
        JdbcTemplate jdbcTemplate = this.getJdbcTemplate(jltJobDetail);
        if (!CollectionUtils.isEmpty(dataMap)) {
            Iterator iterator = dataMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                List<Object> dataList = (List<Object>) entry.getValue();
                if (dataList.size() > 0) {
                    try {
                        jdbcTemplate.batchUpdate(dataWriteDto.getWriteSql(), dataWriteDto.getParams());
                        jltJobDetail.setLastExecSuccessTime(now);
                        jltJobDetail.setLastExecResult(YesNo.YES.getUpperKey());
                        jltJobDetail.setMsg(null);
                        jltJobDetailBiz.update(jltJobDetail);
                    } catch (Exception e) {
                        jltJobDetail.setLastExecResult(YesNo.NO.getUpperKey());
                        jltJobDetail.setMsg(e.getMessage());
                        jltJobDetailBiz.update(jltJobDetail);
                        return new AsyncResult<>(dataWriteDto);
                    }
                }
            }
        }
        return new AsyncResult<>(dataWriteDto);
    }

    private String getReadSql(JltJobDetail jltJobDetail, Long tenantId, String lastExecSuccessTime, Date now) {
        String nowDateFormat = TimeUtil.getStringFromTime(now, "yyyy-MM-dd HH:mm:ss");
        String sql;
        StringBuffer sqlSb = new StringBuffer("");
        String tableName = jltJobDetail.getTableName();
        sqlSb.append("select * from ");
        //如果有自定义SQL
        if (!StringUtils.isEmpty(jltJobDetail.getCustomerSql())) {
            sqlSb.append("(").append(jltJobDetail.getCustomerSql()).append(") tab ");
        } else {
            sqlSb.append(tableName);
        }
        sqlSb.append(" where 1=1 ");
        if (YesNo.NO.getUpperKey().equals(jltJobDetail.getIsExcludeTenantId())) {
            sqlSb.append(" and tenant_id in( ");
            if (!StringUtils.isEmpty(jltJobDetail.getTenantIdCondition())) {
                sqlSb.append(jltJobDetail.getTenantIdCondition());
            } else {
                sqlSb.append(tenantId);
            }
            sqlSb.append(")");
        }

        if (YesNo.NO.getUpperKey().equals(jltJobDetail.getIsExcludeTime())) {
            sqlSb.append(" and (create_time<'").append(nowDateFormat).append("' or modify_time<'").append(nowDateFormat).append("') ");
            if (lastExecSuccessTime != null) {
                sqlSb.append(" and (create_time >='");
                sqlSb.append(lastExecSuccessTime);
                sqlSb.append("' or modify_time>='");
                sqlSb.append(lastExecSuccessTime);
                sqlSb.append("')");
            }
        }
        //添加扩展Where条件
        if (!StringUtils.isEmpty(jltJobDetail.getExtendWhereCondition())) {
            sqlSb.append(" ").append(jltJobDetail.getExtendWhereCondition());
        }
        sql = sqlSb.toString().replaceAll(Globals.TENANT_ID_REPLACE_STR, tenantId.toString());
        //logger.info("【" + jltJobDetail.getTableName() + "】：" + sql);
        return sql;
    }

    @Override
    public DataWriteDto getDataWriteDto(JltJobDetail jltJobDetail, Map.Entry<String, Object> entry) {
        JdbcTemplate jdbcTemplate = this.getJdbcTemplate(jltJobDetail);
        DataWriteDto dataWriteDto = new DataWriteDto();
        List<String> allColumns = new ArrayList<>();
        List<String> updateColumns = new ArrayList<>();
        List<Object[]> params = new ArrayList<>();
        String tableName = entry.getKey();
        //获取目标表字段
        Set<String> targetTableColumns = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from " + tableName + " limit 0");
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        List<String> noInsertColumnsList = new ArrayList<>();
        if (!StringUtils.isEmpty(jltJobDetail.getNoInsertColumns())) {
            noInsertColumnsList = CollectionUtils.arrayToList(jltJobDetail.getNoInsertColumns().split(","));
        }
        List<String> noUpdateColumnsList = new ArrayList<>();
        if (!StringUtils.isEmpty(jltJobDetail.getNoUpdateColumns())) {
            noUpdateColumnsList = CollectionUtils.arrayToList(jltJobDetail.getNoUpdateColumns().split(","));
        }
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (CollectionUtils.isEmpty(noInsertColumnsList) || !noInsertColumnsList.contains(metaData.getColumnName(i))) {
                targetTableColumns.add(metaData.getColumnName(i));
            }
        }

        String sql = "insert into ";
        sql += " " + tableName + "( ";
        List<Map<String, Object>> record = (List<Map<String, Object>>) entry.getValue();
        for (int j = 0; j < record.size(); j++) {
            List<Object> dataList = new ArrayList<>();
            List<Object> updateDataList = new ArrayList<>();
            Map<String, Object> map = record.get(j);
            Iterator colItr = map.entrySet().iterator();
            while (colItr.hasNext()) {
                Map.Entry<String, Object> data = (Map.Entry<String, Object>) colItr.next();
                //只有目标表存在的字段，才更新。源表新增的字段不会更新到目标表，保证SQL不会出错
                if (targetTableColumns.contains(data.getKey())) {
                    if (j == 0) {
                        allColumns.add(data.getKey());
                        if (!noUpdateColumnsList.contains(data.getKey())) {
                            updateColumns.add(data.getKey());
                        }
                    }
                    dataList.add(data.getValue());
                    if (!noUpdateColumnsList.contains(data.getKey())) {
                        updateDataList.add(data.getValue());
                    }
                }
            }
            dataList.addAll(updateDataList);
            params.add(dataList.toArray());
        }
        String columnStr = StringUtils.collectionToCommaDelimitedString(allColumns);
        sql += columnStr + ")values(";
        for (int j = 0; j < allColumns.size(); j++) {
            if (j == 0) {
                sql += "?";
            } else {
                sql += ",?";
            }
        }
        sql += ") ON DUPLICATE KEY UPDATE ";
        for (int k = 0; k < updateColumns.size(); k++) {
            String updateColumn = updateColumns.get(k);
            if (k == 0) {
                sql += updateColumn + "=? ";
            } else {
                sql += "," + updateColumn + "=? ";
            }
        }

        dataWriteDto.setWriteSql(sql.replaceAll(Globals.TENANT_ID_REPLACE_STR, jltJobDetail.getTenantId().toString()));
        dataWriteDto.setParams(params);
        return dataWriteDto;
    }

    private JdbcTemplate getJdbcTemplate(JltJobDetail jltJobDetail) {
        JdbcTemplate jdbcTemplate;
        if (DataSchema.SAAS.getKey().equals(jltJobDetail.getSchemaName())) {
            jdbcTemplate = saasJdbcTemplate;
        } else if (DataSchema.UPM.getKey().equals(jltJobDetail.getSchemaName())) {
            jdbcTemplate = upmJdbcTemplate;
        } else {
            jdbcTemplate = pmsJdbcTemplate;
        }
        return jdbcTemplate;
    }
}
