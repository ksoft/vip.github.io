package com.jlt.datasync.controllers;

import com.jlt.common.constant.Globals;
import com.jlt.common.enums.DataSchema;
import com.jlt.common.enums.JobType;
import com.jlt.common.util.ApiResultMessage;
import com.jlt.datasync.biz.DataHandlerBiz;
import com.jlt.datasync.biz.JltJobBiz;
import com.jlt.datasync.biz.JltJobDetailBiz;
import com.jlt.datasync.condition.JltJobCondition;
import com.jlt.datasync.condition.JltJobDetailCondition;
import com.jlt.datasync.domain.JltJob;
import com.jlt.datasync.domain.JltJobDetail;
import com.jlt.datasync.dto.DataTransferDto;
import com.jlt.datasync.dto.DataWriteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.Future;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Controller
@RequestMapping(value = "/dataReceive")
public class DataReceiveController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean isRunning = false;

    @Autowired
    private DataHandlerBiz dataHandlerBiz;
    @Autowired
    private JltJobBiz jltJobBiz;
    @Autowired
    private JltJobDetailBiz jltJobDetailBiz;

    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    @ResponseBody
    public ApiResultMessage receive(@RequestBody DataTransferDto dataTransferDto) {
        logger.info("接收到新数据，开始执行写入程序");
        if (isRunning) {
            logger.info("数据同步任务已经在执行，本次任务退出");
            return new ApiResultMessage(Globals.ERROR, "定时任务已经在执行");
        }
        Date now = new Date();
        try {
            isRunning = true;
            JltJobCondition jltJobCondition = new JltJobCondition();
            jltJobCondition.setId(1L);
            JltJob jltJob = jltJobBiz.findOne(jltJobCondition);
            this.doWriteJob(jltJob, dataTransferDto.getSaasDataMap(), now, DataSchema.SAAS);
            this.doWriteJob(jltJob, dataTransferDto.getUpmDataMap(), now, DataSchema.UPM);
            this.doWriteJob(jltJob, dataTransferDto.getPmsDataMap(), now, DataSchema.PMS);
        } catch (Exception e) {
            logger.error("数据写入失败："+e.getMessage());
            return new ApiResultMessage(Globals.ERROR, e.getMessage());
        } finally {
            isRunning = false;
        }
        logger.info("数据写入成功");
        return new ApiResultMessage(Globals.SUCCESS);
    }

    private void doWriteJob(JltJob jltJob, Map<String, List<Map<String, Object>>> dataMap, Date now,
            DataSchema dataSchema) {
        if (!CollectionUtils.isEmpty(dataMap)) {
            logger.info("开始写入:"+dataSchema.getKey()+"数据");
            Iterator iterator = dataMap.entrySet().iterator();
            List<Future<DataWriteDto>> futureList = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                List<Object> dataList = (List<Object>) entry.getValue();
                if (dataList.size() > 0) {
                    String tableName = entry.getKey();
                    JltJobDetailCondition jltJobDetailCondition = new JltJobDetailCondition();
                    jltJobDetailCondition.setJobId(jltJob.getId());
                    jltJobDetailCondition.setSchemaName(dataSchema.getKey());
                    jltJobDetailCondition.setJobType(JobType.WRITE.getKey());
                    jltJobDetailCondition.setTableName(tableName);
                    JltJobDetail jltJobDetail = jltJobDetailBiz.findOne(jltJobDetailCondition);
                    if (jltJobDetail != null) {
                        DataWriteDto dataWriteDto = dataHandlerBiz.getDataWriteDto(jltJobDetail, entry);
                        Future<DataWriteDto> future = dataHandlerBiz.writeData(jltJobDetail, dataMap, now, dataWriteDto);
                        logger.info("add a future,tableName：" + jltJobDetail.getSchemaName() + "." + jltJobDetail.getTableName());
                        futureList.add(future);
                    } else {
                        logger.info("job_id=:" + jltJob.getId() + ",schema_name=:" + dataSchema.getKey() + ",table_name=:" + tableName
                                + " 的job_detail WRITE数据不存在。");
                    }
                }
            }
            while (true) {
                logger.info("loop wait for "+futureList.size()+" futures done....");
                try {
                    Iterator<Future<DataWriteDto>> futureIterator = futureList.iterator();

                    while (futureIterator.hasNext()) {
                        Future<DataWriteDto> future = futureIterator.next();
                        if (future.isDone()) {
                            futureIterator.remove();
                            logger.info("end a future,now futureList.size=" + futureList.size());
                        }
                    }
                    if (futureList.size() == 0) {
                        logger.info("all futures done");
                        break;
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    logger.error("写数据失败：" + e.getMessage());
                    break;
                }
            }
        }
    }
}
