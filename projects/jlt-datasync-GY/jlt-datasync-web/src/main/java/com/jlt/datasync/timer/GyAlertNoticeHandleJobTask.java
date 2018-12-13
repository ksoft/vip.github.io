package com.jlt.datasync.timer;

import com.jlt.common.enums.YesNo;
import com.jlt.datasync.biz.EtWaybillExceptionBiz;
import com.jlt.datasync.biz.GyAlertNoticeSettingBiz;
import com.jlt.datasync.biz.MonAlertRuleBiz;
import com.jlt.datasync.condition.EtWaybillExceptionCondition;
import com.jlt.datasync.condition.GyAlertNoticeSettingCondition;
import com.jlt.datasync.condition.MonAlertRuleCondition;
import com.jlt.datasync.domain.MonAlertRule;
import com.jlt.datasync.dto.EtWaybillExceptionDto;
import com.jlt.datasync.dto.GyAlertNoticeSettingDto;
import com.jlt.ums.facade.message.UmsMessageFacade;
import com.jlt.ums.facade.message.bo.EsMessageBO;
import com.sinoservices.common.date.DateJDK8SafeUtil;
import com.sinoservices.minima.common.sequence.IdGenerator;
import com.sinoservices.minima.common.util.EmptyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 温度报警通知定时任务
 *
 * @author Billy.Zhang
 * @date 2018/11/20
 */
@Component
@JobHander("gy:alertNoticeHandle")
public class GyAlertNoticeHandleJobTask extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UmsMessageFacade umsMessageFacade;
    @Autowired
    private EtWaybillExceptionBiz etWaybillExceptionBiz;
    @Autowired
    private GyAlertNoticeSettingBiz gyAlertNoticeSettingBiz;
    @Autowired
    private MonAlertRuleBiz monAlertRuleBiz;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("开始执行温度报警通知，定时任务");
        dataHandleTaskEntrance();
        logger.info("温度报警通知定时任务，执行成功");
        return ReturnT.SUCCESS;
    }

    /**
     * 处理数据的入口
     */
    private void dataHandleTaskEntrance() {
        MonAlertRuleCondition monAlertRuleCondition = new MonAlertRuleCondition();
        monAlertRuleCondition.setAlertRuleCode("TEMPERATURE_EXCEEDED_ALARM");
        MonAlertRule monAlertRule = monAlertRuleBiz.findOne(monAlertRuleCondition);
        if (monAlertRule == null) {
            logger.info("未查询到温度预报警设置数据，本次任务退出。");
            return;
        }
        if (monAlertRule.getIsTemperature() == null || YesNo.NO.getLowerKey().equals(monAlertRule.getIsTemperature())) {
            logger.info("温度预报警设置未启用，本次任务退出。");
            return;
        }
        if (monAlertRule.getIsMessage() == null || YesNo.NO.getLowerKey().equals(monAlertRule.getIsMessage())) {
            logger.info("未勾选站内信复选框，本次任务退出。");
            return;
        }

        //1.获取通知设置
        GyAlertNoticeSettingCondition gyAlertNoticeSettingCondition = new GyAlertNoticeSettingCondition();
        List<GyAlertNoticeSettingDto> gyAlertNoticeSettingDtoList = gyAlertNoticeSettingBiz.findList(gyAlertNoticeSettingCondition);
        if (EmptyUtil.isEmpty(gyAlertNoticeSettingDtoList)) {
            logger.info("未查询到通知设置数据或通知设置的帐号不存在，本次任务退出。");
            return;
        }
        //2.获取N天内温度异常数据（未处理过的）
        String startTime = DateJDK8SafeUtil.getPreviousDateByDays(30) + " 00:00:00";
        String endTime = DateJDK8SafeUtil.getNowDate() + " 23:59:59";
        EtWaybillExceptionCondition etWaybillExceptionCondition = new EtWaybillExceptionCondition();
        etWaybillExceptionCondition.setStartTime(startTime);
        etWaybillExceptionCondition.setEndTime(endTime);
        List<EtWaybillExceptionDto> etWaybillExceptionDtoList = etWaybillExceptionBiz.findTemperatureList(etWaybillExceptionCondition);
        if (EmptyUtil.isEmpty(etWaybillExceptionDtoList)) {
            logger.info("未查询到需要处理的温度报警数据，本次任务退出。");
            return;
        }
        //3.匹配通知设置
        List<EsMessageBO> esMessageBOList = new ArrayList<>();
        List<EtWaybillExceptionDto> etWaybillExceptionDtoUpdateList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (EtWaybillExceptionDto etWaybillExceptionDto : etWaybillExceptionDtoList) {
            LocalDateTime exceptionTime = LocalDateTime
                    .parse(etWaybillExceptionDto.getExceptionTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
            Long minutes = ChronoUnit.MINUTES.between(exceptionTime, now);
            //异常时间到当前时间间隔的小时数
            BigDecimal hours = BigDecimal.valueOf(minutes / 60).setScale(1, BigDecimal.ROUND_HALF_UP);
            for (GyAlertNoticeSettingDto settingDto : gyAlertNoticeSettingDtoList) {
                if (hours.compareTo(settingDto.getPrescriptionFrom()) > 0
                        && hours.compareTo(settingDto.getPrescriptionTo()) <= 0) {// >from && <= to
                    //如果该异常未通知过，或异常通知人与上一次不一样，需要发送站内信
                    if (etWaybillExceptionDto.getLastNoticeSettingId() == null || !etWaybillExceptionDto.getLastNoticeSettingId()
                            .equals(settingDto.getId())) {
                        EsMessageBO esMessageBO = new EsMessageBO();
                        esMessageBO.setEsmgId(IdGenerator.nextId());
                        esMessageBO.setMsgType("10");//类型(10:系统预警;20：系统提醒)
                        esMessageBO.setOrgId(etWaybillExceptionDto.getTenantId().toString());
                        esMessageBO.setTitle("温度超标");
                        esMessageBO.setContent(etWaybillExceptionDto.getExceptionContent());
                        esMessageBO.setRelationId(settingDto.getPlatformAccount());
                        esMessageBO.setCreator("定时器");
                        esMessageBOList.add(esMessageBO);
                        //更新异常上一次通知人id
                        etWaybillExceptionDto.setLastNoticeSettingId(settingDto.getId());
                        etWaybillExceptionDtoUpdateList.add(etWaybillExceptionDto);
                    }
                }
            }
        }
        if (EmptyUtil.isNotEmpty(esMessageBOList)) {
            umsMessageFacade.batchInsert(esMessageBOList);
            logger.info("新增消息数：" + esMessageBOList.size());
            etWaybillExceptionBiz.batchUpdateLastNoticeSettingId(etWaybillExceptionDtoUpdateList);
        }
    }
}
