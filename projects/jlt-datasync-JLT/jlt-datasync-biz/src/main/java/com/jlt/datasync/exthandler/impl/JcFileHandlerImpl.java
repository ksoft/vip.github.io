package com.jlt.datasync.exthandler.impl;

import com.jlt.common.annotation.DataReadHandler;
import com.jlt.common.constant.Globals;
import com.jlt.common.enums.YesNo;
import com.jlt.datasync.domain.JltJobDetail;
import com.jlt.datasync.exthandler.IExtDataReadHandler;
import com.sinoservices.minima.common.util.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Billy.Zhang
 * @date 2018/10/26
 */
@Component
@DataReadHandler("jc_file")
public class JcFileHandlerImpl extends IExtDataReadHandler {

    @Override
    public List<Map<String, Object>> process(JltJobDetail jltJobDetail, String lastExecSuccessTime, Date now, String... var1)
            throws Exception {
        List<Map<String, Object>> resultDataList = new ArrayList<>();
        String nowDateFormat = DateUtil.format(now);
        StringBuffer sqlSaas = new StringBuffer("");
        sqlSaas.append("SELECT a.jf_pm_code ");
        sqlSaas.append("FROM et_file_ref a ");
        sqlSaas.append("INNER JOIN et_waybill_info b ON a.business_pm_code = b.waybill_no ");
        sqlSaas.append("WHERE b.consigner_id = tenant_id_replace ");
        sqlSaas.append("and a.tenant_id in( ");
        sqlSaas.append("select carrier_id from cd_carrier_config where bind_type=2 and tenant_id =  tenant_id_replace ");
        sqlSaas.append(") ");
        if (YesNo.NO.getUpperKey().equals(jltJobDetail.getIsExcludeTime())) {
            sqlSaas.append(" and (a.create_time<'").append(nowDateFormat).append("' or a.modify_time<'").append(nowDateFormat)
                    .append("') ");
            if (lastExecSuccessTime != null) {
                sqlSaas.append(" and (a.create_time >='");
                sqlSaas.append(lastExecSuccessTime);
                sqlSaas.append("' or a.modify_time>='");
                sqlSaas.append(lastExecSuccessTime);
                sqlSaas.append("')");
            }
        }
        String sql = sqlSaas.toString().replaceAll(Globals.TENANT_ID_REPLACE_STR, jltJobDetail.getTenantId().toString());
        logger.info("jc_file sql 1:" + sql);
        List<Map<String, Object>> saasDataList = super.saasJdbcTemplate.queryForList(sql);
        List<String> pmCodeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saasDataList)) {
            for (Map<String, Object> map : saasDataList) {
                pmCodeList.add("'" + map.get("jf_pm_code") + "'");
            }
            StringBuffer sqlPms = new StringBuffer("");
            sqlPms.append("select a.* from jc_file a ");
            sqlPms.append("where a.pm_code in( ");
            sqlPms.append(StringUtils.collectionToCommaDelimitedString(pmCodeList));
            sqlPms.append(") ");
            logger.info("jc_file sql 2:" + sqlPms.toString());
            resultDataList = super.pmsJdbcTemplate.queryForList(sqlPms.toString());
        }
        return resultDataList;
    }
}
