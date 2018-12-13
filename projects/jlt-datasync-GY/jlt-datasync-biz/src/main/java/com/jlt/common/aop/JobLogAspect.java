package com.jlt.common.aop;

import com.jlt.common.annotation.JobLog;
import com.jlt.common.enums.DataSchema;
import com.jlt.common.enums.JobType;
import com.jlt.datasync.biz.JltJobDetailLogBiz;
import com.jlt.datasync.domain.JltJobDetail;
import com.jlt.datasync.dto.DataTransferDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Billy.Zhang
 * @date 2018/9/26
 */
@Aspect
@Component
public class JobLogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JltJobDetailLogBiz jltJobDetailLogBiz;

    @Pointcut("@annotation(com.jlt.common.annotation.JobLog)")
    public void jobLog() {
    }

    @Before("jobLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        try {
            JltJobDetail jltJobDetail = (JltJobDetail) joinPoint.getArgs()[0];
            String msg = "";
            if (JobType.READ.getKey().equals(jltJobDetail.getJobType())) {
                msg = "【" + jltJobDetail.getTableName() + "】,开始抽取数据 ";
            } else {
                msg = "【" + jltJobDetail.getTableName() + "】,开始写入数据 ";
            }
            logger.info(msg);
            //jltJobDetailLogBiz.insertLog(jltJobDetail, msg, new Date());
        } catch (Exception e) {
            logger.error("日志处理异常，不影响业务逻辑！错误信息：" + e.getMessage());
        }
    }

    @AfterReturning(returning = "ret", pointcut = "@annotation(jobLog)")
    public void doAfterReturning(JoinPoint joinPoint, Object ret, JobLog jobLog) throws Throwable {
        try {
            JobType jobType = jobLog.jobTye();
            JltJobDetail jltJobDetail = (JltJobDetail) joinPoint.getArgs()[0];
            String msg = "";
            if (JobType.READ.equals(jobType)) {
                DataTransferDto dataTransferDto = (DataTransferDto) joinPoint.getArgs()[1];
                int count = 0;
                List<Map<String, Object>> dataList;
                if (DataSchema.SAAS.getKey().equals(jltJobDetail.getSchemaName())) {
                    dataList = dataTransferDto.getSaasDataMap().get(jltJobDetail.getTableName());
                } else if (DataSchema.UPM.getKey().equals(jltJobDetail.getSchemaName())) {
                    dataList = dataTransferDto.getUpmDataMap().get(jltJobDetail.getTableName());
                } else {
                    dataList = dataTransferDto.getPmsDataMap().get(jltJobDetail.getTableName());
                }
                if (dataList != null) {
                    count = dataList.size();
                }
                msg = "【"+jltJobDetail.getTableName()+"】，抽取数据成功，数量：" + count;
            } else {
                msg += "【" + jltJobDetail.getTableName() + "】，写入数据成功";
            }
            logger.info(msg);
            //jltJobDetailLogBiz.insertLog(jltJobDetail, msg, new Date());
        } catch (Exception e) {
            logger.error("日志处理异常，不影响业务逻辑！错误信息：" + e.getMessage());
        }
    }
}
