package com.jlt.datasync.job;

import com.jlt.common.constant.Globals;
import com.jlt.common.enums.DataSchema;
import com.jlt.common.enums.JobType;
import com.jlt.common.enums.YesNo;
import com.jlt.common.util.ApiResultMessage;
import com.jlt.common.util.HttpClientForSinopharmFollow;
import com.jlt.datasync.biz.DataHandlerBiz;
import com.jlt.datasync.biz.JltJobBiz;
import com.jlt.datasync.biz.JltJobDetailBiz;
import com.jlt.datasync.condition.JltJobCondition;
import com.jlt.datasync.condition.JltJobDetailCondition;
import com.jlt.datasync.domain.JltJob;
import com.jlt.datasync.domain.JltJobDetail;
import com.jlt.datasync.dto.DataTransferDto;
import com.sinoservices.minima.common.util.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Component
@JobHander("jlt-gy:datasync")
public class DataSyncJob extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataHandlerBiz dataReadBiz;
    @Autowired
    private JltJobBiz jltJobBiz;
    @Autowired
    private JltJobDetailBiz jltJobDetailBiz;

    @Value("${jlt.datasync.edi.url}")
    private String ediUrl;

    @Value("${jlt.datasync.recv.url}")
    private String recvUrl;

    /**
     * 程序入口
     * 如果指定要跑从哪个时间开始的数据，需要在xxlJob配置时间参数
     * 格式示例：2018-09-13 16:21:53
     *
     * @param strings
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        JltJobCondition jltJobCondition = new JltJobCondition();
        jltJobCondition.setId(1L);
        JltJob jltJob = jltJobBiz.findOne(jltJobCondition);
        if (jltJob == null) {
            logger.error("id为1 的job 不存在，请确认数据库job配置。此次定时任务退出。");
            return ReturnT.FAIL;
        }
        logger.info("开始执行国药=>晶链通，数据同步任务(tenant_id=" + jltJob.getTenantId() + ")");
        String lastExecSuccessTime = null;
        Integer intervalMinutes = 10;
        if (strings != null) {
            if (strings.length >= 1) {
                lastExecSuccessTime = strings[0];
            }
            if (strings.length >= 2) {
                intervalMinutes = Integer.parseInt(strings[1]);
            }
        }
        DataTransferDto dataTransferDto = new DataTransferDto();
        dataTransferDto.setTenantId(jltJob.getTenantId());
        if (lastExecSuccessTime == null && jltJob.getLastExecSuccessTime() != null) {
            lastExecSuccessTime = DateUtil.format(DateUtils.addMinutes(jltJob.getLastExecSuccessTime(), -10));
        }
        //默认取当前日期
        Date now = new Date();
        //上一次成功成功日期后移 intervalMinutes 分钟
        Date toDate = DateUtils.addMinutes(jltJob.getLastExecSuccessTime(), intervalMinutes);
        if (jltJob.getLastExecSuccessTime() != null && toDate.before(now)) {
            now = toDate;
        }

        dataTransferDto.setStartTime("【晶链通=>国药】(" + lastExecSuccessTime + "=>" + DateUtil.format(now) + ")");
        if (YesNo.YES.getUpperKey().equals(jltJob.getIsEnableSaas())) {
            this.doReadJob(jltJob, dataTransferDto, lastExecSuccessTime, now, DataSchema.SAAS);
        }
        if (YesNo.YES.getUpperKey().equals(jltJob.getIsEnableUpm())) {
            this.doReadJob(jltJob, dataTransferDto, lastExecSuccessTime, now, DataSchema.UPM);
        }
        if (YesNo.YES.getUpperKey().equals(jltJob.getIsEnablePms())) {
            this.doReadJob(jltJob, dataTransferDto, lastExecSuccessTime, now, DataSchema.PMS);
        }

        if (CollectionUtils.isEmpty(dataTransferDto.getSaasDataMap()) && CollectionUtils.isEmpty(dataTransferDto.getUpmDataMap())
                && CollectionUtils.isEmpty(dataTransferDto.getPmsDataMap())) {
            //更新JOB时间和状态
            jltJob.setLastExecSuccessTime(now);
            jltJob.setLastExecResult(YesNo.YES.getUpperKey());
            jltJob.setMsg(null);
            jltJobBiz.update(jltJob);
            logger.info("未查询到任何新数据，晶链通=>国药，数据同步任务执行成功");
            return ReturnT.SUCCESS;
        }

        //发送数据
        try {
            /*logger.info("开始调用EDI发送数据");
            logger.info("ediUrl:" + ediUrl);
            ApiResultMessage resultMessage = HttpClientForSinopharmFollow.excute(dataTransferDto, ediUrl);*/

            logger.info("开始发送数据（未调用EDI）");
            logger.info("recvUrl:" + recvUrl);
            ApiResultMessage resultMessage = HttpClientForSinopharmFollow.excuteWithoutEdi(dataTransferDto, recvUrl);
            if (Globals.SUCCESS.equals(resultMessage.getStatus())) {
                //更新JOB时间和状态
                jltJob.setLastExecSuccessTime(now);
                jltJob.setLastExecResult(YesNo.YES.getUpperKey());
                jltJob.setMsg(null);
                jltJobBiz.update(jltJob);
                logger.info("晶链通=>国药，数据同步任务执行成功");
                return ReturnT.SUCCESS;
            } else {
                jltJob.setLastExecSuccessTime(now);
                jltJob.setLastExecResult(YesNo.NO.getUpperKey());
                jltJob.setMsg(resultMessage.getMessage());
                jltJobBiz.update(jltJob);
                logger.error("晶链通=>国药，数据同步任务执行失败:" + resultMessage.getMessage());
                return ReturnT.FAIL;
            }
        } catch (Exception e) {
            jltJob.setLastExecSuccessTime(now);
            jltJob.setLastExecResult(YesNo.NO.getUpperKey());
            jltJob.setMsg(e.getMessage());
            jltJobBiz.update(jltJob);
            logger.error("晶链通=>国药，数据同步任务执行失败2:" + e.getMessage());
            return ReturnT.FAIL;
        }
    }



    private void doReadJob(JltJob jltJob, DataTransferDto dataTransferDto, String lastExecSuccessTime, Date now, DataSchema dataSchema) {
        JltJobDetailCondition jltJobDetailCondition = new JltJobDetailCondition();
        jltJobDetailCondition.setJobId(jltJob.getId());
        jltJobDetailCondition.setSchemaName(dataSchema.getKey());
        jltJobDetailCondition.setJobType(JobType.READ.getKey());
        List<JltJobDetail> jobDetailList = jltJobDetailBiz.findList(jltJobDetailCondition);
        List<Future<DataTransferDto>> futureList = new ArrayList<>();
        for (JltJobDetail jltJobDetail : jobDetailList) {
            Future<DataTransferDto> future = dataReadBiz.readData(jltJobDetail, dataTransferDto, lastExecSuccessTime, now);
            futureList.add(future);
        }
        while (true) {
            logger.info("loop wait for " + futureList.size() + " futures done....");
            try {
                Iterator<Future<DataTransferDto>> futureIterator = futureList.iterator();
                while (futureIterator.hasNext()) {
                    Future<DataTransferDto> future = futureIterator.next();
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
                logger.error("读取数据失败..：" + e.getMessage());
                break;
            }
        }
    }
}
