package com.jlt.datasync.timer;

import com.jlt.common.constant.Globals;
import com.jlt.common.enums.AlarmWarning;
import com.jlt.common.enums.AlertRuleTypeEnum;
import com.jlt.common.enums.TransportModel;
import com.jlt.common.enums.YesNo;
import com.jlt.datasync.biz.*;
import com.jlt.datasync.condition.*;
import com.jlt.datasync.domain.*;
import com.jlt.datasync.dto.EtWaybillExceptionDto;
import com.jlt.ums.facade.message.UmsMessageFacade;
import com.jlt.ums.facade.message.bo.EsMessageBO;
import com.sinoservices.common.date.DateJDK8SafeUtil;
import com.sinoservices.minima.common.sequence.IdGenerator;
import com.sinoservices.xframework.core.utils.EmptyUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 根据报警规则，生成运单异常（提货时效、送达时效、回单时效、返单时效）
 *
 * @author Billy.Zhang
 * @date 2018/9/18
 */
@Component
@JobHander("gy:waybillexceptionhandle")
public class GyEtWaybillExceptionHandleJobTask extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MonAlertRuleBiz monAlertRuleBiz;
    @Autowired
    private MonAlertRuleItemBiz monAlertRuleItemBiz;
    @Autowired
    private GyjlDispatchOrderBiz gyjlDispatchOrderBiz;
    @Autowired
    private HadleWayBillBiz hadleWayBillBiz;
    @Autowired
    private EtWaybillExceptionBiz etWaybillExceptionBiz;
    @Autowired
    private GyThermometerHistoryBiz gyThermometerHistoryBiz;
    @Autowired
    private UmsMessageFacade umsMessageFacade;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("开始执行预警，定时任务");
        dataHandleTaskEntrance();
        logger.info("预警定时任务，执行成功");
        return ReturnT.SUCCESS;
    }

    /**
     * 处理数据的入口
     */
    private void dataHandleTaskEntrance() {

        // 查询预警时效设置
        MonAlertRuleCondition monAlertCondition = new MonAlertRuleCondition();
        monAlertCondition.setIsEnable(YesNo.YES.getLowerKey());
        List<MonAlertRule> alertRuleModels = monAlertRuleBiz.findByCondition(monAlertCondition);

        // 封装调度单号与运单温度返回情况map集合
        String endDate = DateJDK8SafeUtil.getNowDate() + " 23:59:59";
        String startDate = DateJDK8SafeUtil.getPreviousDateByDays(15) + " 00:00:00";
        getWaybillData(startDate, endDate, alertRuleModels);
    }

    /**
     * 获取运单数据，时间今天之前3天
     *
     * @param startTime
     * @param endTime
     * @param alertRuleModels
     */
    private void getWaybillData(String startTime, String endTime, List<MonAlertRule> alertRuleModels) {
        HandleWayBillCondition billCondition = new HandleWayBillCondition();
        billCondition.setStartTime(startTime);
        billCondition.setEndTime(endTime);
        List<HandleWayBill> billModels = hadleWayBillBiz.getWayBillWithByCondition(billCondition);
        logger.info("获取运单，日期：(" + startTime + "=>" + endTime + ")，获取记录数：" + (EmptyUtils.isEmpty(billModels) ? 0 : billModels.size()));
        if (EmptyUtils.isEmpty(billModels)) {
            return;
        }
        List<String> updateWayBillNos = new ArrayList<>();
        // 获取派车单号集合
        Set<String> owbEcNos = billModels.stream().map(HandleWayBill::getOwbEcNo).collect(Collectors.toSet());
        List<GyjlDispatchOrder> dispatchOrderList = new ArrayList<>();
        List<HandleWayBill> etWaybillInfoUpdateList = new ArrayList<>();
        List<EtWaybillException> etWaybillExceptionInsertList = new ArrayList<>();
        List<GyThermometerHistory> thermometerHistoryUpdateList = new ArrayList<>();
        List<EtWaybillExceptionDto> etWaybillExceptionUpdateList = new ArrayList<>();
        for (String owbEcNo : owbEcNos) {
            Integer overTimeCount = 0;
            Integer warnCount = 0;
            List<HandleWayBill> handleWayBills = this.filterWayBillByOwbEcNo(owbEcNo, billModels);
            for (HandleWayBill handleWayBill : handleWayBills) {
                for (MonAlertRule alertRule : alertRuleModels) {
                    Map<String, Integer> countMap =
                            this.packageWaybillException(etWaybillExceptionInsertList, alertRule, handleWayBill, updateWayBillNos,
                                    thermometerHistoryUpdateList, etWaybillExceptionUpdateList);
                    Integer oneWaybillOverTimeCount = countMap.get(Globals.ONE_WAYBILL_OVER_TIME_COUNT);
                    Integer oneWaybillWarnCount = countMap.get(Globals.ONE_WAYBILL_WARN_COUNT);
                    overTimeCount += oneWaybillOverTimeCount;
                    warnCount += oneWaybillWarnCount;
                }
                //logger.info("warnCount:" + warnCount + ",overTimeCount:" + overTimeCount);
                if (warnCount > 0 || overTimeCount > 0) {
                    handleWayBill.setWaybillException("0");// 运单是否异常(0 是 1否)
                    etWaybillInfoUpdateList.add(handleWayBill);
                }
            }
            //logger.info("total warnCount:" + warnCount + ",total overTimeCount:" + overTimeCount);
            // 根据派车单号，查出派车单
            GyjlDispatchOrder dispatchOrder = gyjlDispatchOrderBiz.findByOwbEcNo(owbEcNo);
            if (null != dispatchOrder && warnCount > 0 && dispatchOrder.getTrackStatus() != 20) {
                dispatchOrder.setTrackStatus(10);// 10 超时预警
            }
            if (null != dispatchOrder && overTimeCount > 0) {
                dispatchOrder.setTrackStatus(20);// 20已超时
            }
            dispatchOrderList.add(dispatchOrder);
        }
        // 更新派车单的时效跟踪
        if (!CollectionUtils.isEmpty(dispatchOrderList)) {
            gyjlDispatchOrderBiz.batchUpdate(dispatchOrderList);
        }
        // 更新运单异常状态、是否已预警/已报警
        if (!CollectionUtils.isEmpty(etWaybillInfoUpdateList)) {
            hadleWayBillBiz.batchUpdateWaybillInfo(etWaybillInfoUpdateList);
        }
        //更新温度计记录表，是否已预警/已报警
        if (!CollectionUtils.isEmpty(thermometerHistoryUpdateList)) {
            gyThermometerHistoryBiz.batchUpdate(thermometerHistoryUpdateList);
        }
        //更新异常表，是否已预警/已报警
        if (!CollectionUtils.isEmpty(etWaybillExceptionUpdateList)) {
            etWaybillExceptionBiz.batchUpdateIsWarningOrAlarm(etWaybillExceptionUpdateList);
        }
        // 插入新异常
        if (!CollectionUtils.isEmpty(etWaybillExceptionInsertList)) {
            etWaybillExceptionBiz.batchInsert(etWaybillExceptionInsertList);
            //新增站内信
            List<EsMessageBO> esMessageBOList = new ArrayList<>();
            for (EtWaybillException exception : etWaybillExceptionInsertList) {
                EsMessageBO esMessageBO = new EsMessageBO();
                esMessageBO.setEsmgId(IdGenerator.nextId());
                esMessageBO.setMsgType("10");//类型(10:系统预警;20：系统提醒)
                esMessageBO.setOrgId(exception.getTenantId().toString());
                esMessageBO.setTitle(exception.getExceptionTypeName());
                esMessageBO.setContent(exception.getExceptionContent());
                esMessageBO.setRelationId(exception.getTenantId().toString());
                esMessageBO.setCreator("定时器");
                esMessageBOList.add(esMessageBO);
            }
            umsMessageFacade.batchInsert(esMessageBOList);
            logger.info("新增消息数：" + esMessageBOList.size());
        }
        // 更新运单ext gy_background_caolor
        if (EmptyUtils.isNotEmpty(updateWayBillNos)) {
            gyjlDispatchOrderBiz.batchUpdateWayBillDetail(updateWayBillNos);
        }
        // 更新hasException
        hadleWayBillBiz.batchUpdateHasException();
    }

    private List<HandleWayBill> filterWayBillByOwbEcNo(String owbEcNo, List<HandleWayBill> handleWayBillList) {
        Predicate<HandleWayBill> filter = model -> owbEcNo.equals(model.getOwbEcNo());
        return handleWayBillList.stream().filter(filter).collect(Collectors.toList());
    }

    private void packageExcetionData(List<EtWaybillException> etWaybillExceptionInsertList, String thermometerNo,
            HandleWayBill handleWayBill, AlertRuleTypeEnum alertRuleTypeEnum, Long minutes, LocalDateTime now, AlarmWarning alarmWarning) {
        String typeName = AlertRuleTypeEnum.getTypeName(alertRuleTypeEnum);
        String alarmOrWarningStr;
        if (AlarmWarning.WARNING.equals(alarmWarning)) {
            alarmOrWarningStr = AlarmWarning.WARNING.getValue();
        } else {
            // alarmOrWarningStr = AlarmWarning.ALARM.getValue();
            alarmOrWarningStr = "超时";
        }
        logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，新增" + typeName + alarmOrWarningStr + "异常");
        switch (alertRuleTypeEnum) {
            case PICKUP_WARNING:
                if (AlarmWarning.WARNING.equals(alarmWarning)) {
                    handleWayBill.setIsPickupEarlyWarning(YesNo.YES.getUpperKey());
                } else {
                    handleWayBill.setIsPickupAlarm(YesNo.YES.getUpperKey());
                }
                break;
            case ARRIVAL_WARNING:
                if (AlarmWarning.WARNING.equals(alarmWarning)) {
                    handleWayBill.setIsArrivalEarlyWarning(YesNo.YES.getUpperKey());
                } else {
                    handleWayBill.setIsArrivalAlarm(YesNo.YES.getUpperKey());
                }
                break;
            case RECEIPT_WARNING:
                if (AlarmWarning.WARNING.equals(alarmWarning)) {
                    handleWayBill.setIsReceiptEarlyWarning(YesNo.YES.getUpperKey());
                } else {
                    handleWayBill.setIsReceiptAlarm(YesNo.YES.getUpperKey());
                }
                break;
            case RETURN_WARNING:
                if (AlarmWarning.WARNING.equals(alarmWarning)) {
                    handleWayBill.setIsReturnEarlyWarning(YesNo.YES.getUpperKey());
                } else {
                    handleWayBill.setIsReturnAlarm(YesNo.YES.getUpperKey());
                }
                break;
            default:
        }
        // 生成运单异常记录
        EtWaybillException etWaybillException = new EtWaybillException();
        etWaybillException.setTenantId(Long.parseLong(handleWayBill.getOwbOfficeId()));
        etWaybillException.setWaybillNo(handleWayBill.getOwbWayBillNo());
        etWaybillException.setExWaybillNo(handleWayBill.getExWayBillNo());
        etWaybillException.setExceptionNo("GYEX" + IdGenerator.nextId());
        etWaybillException.setWaybillStatus(handleWayBill.getWaybillStatus());
        etWaybillException.setExceptionType(alertRuleTypeEnum.getKey());
        etWaybillException.setExceptionTypeName(typeName + alarmOrWarningStr);
        etWaybillException.setExceptionTime(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)));
        String requireTime = getRequireTime(alertRuleTypeEnum, handleWayBill);
        StringBuffer exceptionContent = new StringBuffer("");
        exceptionContent.append("运单号：").append(handleWayBill.getExWayBillNo()).append("，");
        if (alertRuleTypeEnum.getKey().equals(AlertRuleTypeEnum.THERMOMETER_RETURN_WARNING.getKey())) {
            exceptionContent.append("温度计编号：").append(thermometerNo).append("，");
        }
        exceptionContent.append("司机：").append(handleWayBill.getOwbDriverName()).append("，");
        exceptionContent.append("车辆：").append(handleWayBill.getOwbCarNo()).append("，");
        if (AlarmWarning.WARNING.equals(alarmWarning)) {
            exceptionContent.append("即将于 ").append(minutes).append("分钟内").append(typeName).append("超时，");
        } else {
            exceptionContent.append("已").append(typeName).append("超时，");
        }
        exceptionContent.append("要求").append(typeName).append("时间为：").append(requireTime).append("，请及时处理！");
        etWaybillException.setExceptionContent(exceptionContent.toString());
        etWaybillException.setGyHandleFlag(0);
        etWaybillException.setCreator("定时器");
        etWaybillException.setCreateName("定时器");
        etWaybillExceptionInsertList.add(etWaybillException);
    }

    private String getRequireTime(AlertRuleTypeEnum alertRuleTypeEnum, HandleWayBill handleWayBill) {
        String requireTime = null;
        switch (alertRuleTypeEnum) {
            case PICKUP_WARNING:
                requireTime = handleWayBill.getRequireDeliveryTime();
                break;
            case ARRIVAL_WARNING:
                requireTime = handleWayBill.getGyRequireArrivalTime();
                break;
            case RECEIPT_WARNING:
                requireTime = handleWayBill.getGyRequireReceiptUploadTime();
                break;
            case RETURN_WARNING:
                requireTime = handleWayBill.getGyRequireReceiptReturnTime();
                break;
            case RETURN_GOODS_WARNING:
                requireTime = handleWayBill.getGyRequireReturnTime();
                break;
            case THERMOMETER_RETURN_WARNING:
                requireTime = handleWayBill.getGyRequireThermometerReturnTime();
                break;
        }
        return requireTime;
    }

    private Map<String, Integer> packageWaybillException(List<EtWaybillException> etWaybillExceptionInsertList, MonAlertRule alertRule,
            HandleWayBill handleWayBill, List<String> updateWayBillNos, List<GyThermometerHistory> thermometerHistoryUpdateList,
            List<EtWaybillExceptionDto> etWaybillExceptionUpdateList) {
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put(Globals.ONE_WAYBILL_OVER_TIME_COUNT, 0);
        countMap.put(Globals.ONE_WAYBILL_WARN_COUNT, 0);

        MonAlertRuleItemCondition monAlertRuleItemCondition = new MonAlertRuleItemCondition();
        monAlertRuleItemCondition.setAlertRulePmCode(alertRule.getPmCode());
        List<MonAlertRuleItem> alertRuleItemList = monAlertRuleItemBiz.findByCondition(monAlertRuleItemCondition);
        LocalDateTime now = LocalDateTime.now();
        // 1.提货时效预报警
        if (AlertRuleTypeEnum.PICKUP_WARNING.getKey().equals(alertRule.getAlertRuleCode())) {
            this.packagePickupWarning(etWaybillExceptionInsertList, handleWayBill, updateWayBillNos, alertRuleItemList, now, countMap);
        }
        // 2.送达时效预报警
        if (AlertRuleTypeEnum.ARRIVAL_WARNING.getKey().equals(alertRule.getAlertRuleCode())) {
            this.packageArrivalWarning(etWaybillExceptionInsertList, handleWayBill, updateWayBillNos, alertRuleItemList, now, countMap);
        }
        // 3.回单时效预报警
        if (AlertRuleTypeEnum.RECEIPT_WARNING.getKey().equals(alertRule.getAlertRuleCode())) {
            this.packageReceiptWarning(etWaybillExceptionInsertList, handleWayBill, updateWayBillNos, alertRuleItemList, now, countMap);
        }
        // 4.返单时效预报警
        if (AlertRuleTypeEnum.RETURN_WARNING.getKey().equals(alertRule.getAlertRuleCode())) {
            this.packageReturnWarning(etWaybillExceptionInsertList, handleWayBill, updateWayBillNos, alertRuleItemList, now, countMap);
        }
        //5.退货时效预报警
        if (AlertRuleTypeEnum.RETURN_GOODS_WARNING.getKey().equals(alertRule.getAlertRuleCode())) {
            this.packageReturnGoodsWarning(etWaybillExceptionInsertList, handleWayBill, updateWayBillNos, alertRuleItemList, now,
                    etWaybillExceptionUpdateList, countMap);
        }
        // 6.温度计返回时效预报警
        if (AlertRuleTypeEnum.THERMOMETER_RETURN_WARNING.getKey().equals(alertRule.getAlertRuleCode())) {
            this.packageThermometerReturnWarning(etWaybillExceptionInsertList, handleWayBill, updateWayBillNos, alertRuleItemList, now,
                    thermometerHistoryUpdateList, countMap);
        }
        return countMap;
    }

    /**
     * 提货时效预报警
     *
     * @param etWaybillExceptionInsertList
     * @param handleWayBill
     * @param updateWayBillNos
     * @param alertRuleItemList
     * @param now
     * @param countMap
     * @return
     */
    private Map<String, Integer> packagePickupWarning(List<EtWaybillException> etWaybillExceptionInsertList, HandleWayBill handleWayBill,
            List<String> updateWayBillNos, List<MonAlertRuleItem> alertRuleItemList, LocalDateTime now, Map<String, Integer> countMap) {
        logger.info("开始执行提货时效预报警");
        // 如果已提货报警过，直接返回
        if (handleWayBill.getIsPickupAlarm() != null && YesNo.YES.getUpperKey().equals(handleWayBill.getIsPickupAlarm())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已提货报警过，直接返回");
            return countMap;
        }
        if (StringUtils.isEmpty(handleWayBill.getRequireDeliveryTime())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，无要求提货时间，直接返回");
            return countMap;
        }
        // 提货超时：要求提货时间到达后还未进行提货，或实际提货时间晚于要求提货时间，则进行报警 有运抵时间就以运抵时间，没有就以签收时间
        if ((handleWayBill.getPickupArriveTime() != null && DateJDK8SafeUtil
                .dateTimeFromIsAfterDateTimeTo(handleWayBill.getPickupArriveTime(), handleWayBill.getRequireDeliveryTime(),
                        DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (handleWayBill.getPickupArriveTime() == null
                && handleWayBill.getPickupTime() == null && DateJDK8SafeUtil
                .dateTimeFromIsAfterDateTimeTo(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)),
                        handleWayBill.getRequireDeliveryTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (
                handleWayBill.getPickupArriveTime() == null && handleWayBill.getPickupTime() != null && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(handleWayBill.getPickupTime(), handleWayBill.getRequireDeliveryTime(),
                                DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))) {
            countMap.put(Globals.ONE_WAYBILL_OVER_TIME_COUNT, countMap.get(Globals.ONE_WAYBILL_OVER_TIME_COUNT) + 1);
            updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
            this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.PICKUP_WARNING, null, now,
                    AlarmWarning.ALARM);
        } else {
            if (!CollectionUtils.isEmpty(alertRuleItemList)) {
                if (handleWayBill.getIsPickupEarlyWarning() != null && YesNo.YES.getUpperKey()
                        .equals(handleWayBill.getIsPickupEarlyWarning())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已提货预警过，直接返回");
                    return countMap;
                }
                // 提货预警：要求提货时间前m分钟内还未进行提货，则进行预警
                LocalDateTime requireDeliveryTime = LocalDateTime
                        .parse(handleWayBill.getRequireDeliveryTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
                Long minutes = ChronoUnit.MINUTES.between(now, requireDeliveryTime);
                if (com.sinoservices.common.other.EmptyUtils.isNotEmpty(alertRuleItemList.get(0).getRuleItemValue())) {
                    Integer validTime = Integer.parseInt(alertRuleItemList.get(0).getRuleItemValue());
                    if (handleWayBill.getPickupTime() == null && minutes <= validTime) {// 预警
                        countMap.put(Globals.ONE_WAYBILL_WARN_COUNT, countMap.get(Globals.ONE_WAYBILL_WARN_COUNT) + 1);
                        updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
                        this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.PICKUP_WARNING,
                                minutes, now, AlarmWarning.WARNING);
                    }
                }
            }
        }
        return countMap;
    }

    /**
     * 送达时效预报警
     *
     * @param etWaybillExceptionInsertList
     * @param handleWayBill
     * @param updateWayBillNos
     * @param alertRuleItemList
     * @param now
     * @param countMap
     * @return
     */
    private Map<String, Integer> packageArrivalWarning(List<EtWaybillException> etWaybillExceptionInsertList, HandleWayBill handleWayBill,
            List<String> updateWayBillNos, List<MonAlertRuleItem> alertRuleItemList, LocalDateTime now, Map<String, Integer> countMap) {
        logger.info("开始执行送达时效预报警");
        // 如果已送达报警过，直接返回
        if (handleWayBill.getIsArrivalAlarm() != null && YesNo.YES.getUpperKey().equals(handleWayBill.getIsArrivalAlarm())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已送达报警过，直接返回");
            return countMap;
        }
        if (StringUtils.isEmpty(handleWayBill.getGyRequireArrivalTime())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，无要求送达时间，直接返回");
            return countMap;
        }
        // 送达超时：要求送达时间到达后还未进行签收，或实际签收时间晚于要求送达时间，则进行报警
        if ((handleWayBill.getShipTime() != null && DateJDK8SafeUtil
                .dateTimeFromIsAfterDateTimeTo(handleWayBill.getShipTime(), handleWayBill.getGyRequireArrivalTime(),
                        DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (handleWayBill.getShipTime() == null
                && handleWayBill.getSignTime() == null && DateJDK8SafeUtil
                .dateTimeFromIsAfterDateTimeTo(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)),
                        handleWayBill.getGyRequireArrivalTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (
                handleWayBill.getShipTime() == null && handleWayBill.getSignTime() != null && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(handleWayBill.getSignTime(), handleWayBill.getGyRequireArrivalTime(),
                                DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))) {
            countMap.put(Globals.ONE_WAYBILL_OVER_TIME_COUNT, countMap.get(Globals.ONE_WAYBILL_OVER_TIME_COUNT) + 1);
            updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
            this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.ARRIVAL_WARNING, null, now,
                    AlarmWarning.ALARM);
        } else {
            if (!CollectionUtils.isEmpty(alertRuleItemList)) {
                if (handleWayBill.getIsArrivalEarlyWarning() != null && YesNo.YES.getUpperKey()
                        .equals(handleWayBill.getIsArrivalEarlyWarning())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已送达预警过，直接返回");
                    return countMap;
                }
                if (StringUtils.isEmpty(handleWayBill.getGyTransportModel())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，运输类型(干线 市内)gyTransportModel无值，直接返回");
                    return countMap;
                }
                Integer validTime = 0;
                for (MonAlertRuleItem alertRuleItem : alertRuleItemList) {
                    if (TransportModel.CITY_LINE_ITEM_CODE.getValue().equals(handleWayBill.getGyTransportModel())) {
                        if (TransportModel.CITY_LINE_ITEM_CODE.getKey().equals(alertRuleItem.getRuleItemCode())) {
                            validTime = Integer.parseInt(alertRuleItem.getRuleItemValue());
                            break;
                        }
                    } else {
                        if (TransportModel.TRUNK_LINE_ITEM_CODE.getKey().equals(alertRuleItem.getRuleItemCode())) {
                            validTime = Integer.parseInt(alertRuleItem.getRuleItemValue());
                            break;
                        }
                    }
                }
                //2018-11-15 先屏蔽送达预警，待线上获取GPS数据校验无误后，再开放此预警
                // 送达预警：未到达要求送达时间，但是预计送达时间与要求送达时间的时间间隔小于等于提前预警时长时，则进行预警
                if (handleWayBill.getPredictArrivalTime() != null && DateJDK8SafeUtil
                        .dateTimeFromIsBeforeDateTimeTo(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)),
                                handleWayBill.getGyRequireArrivalTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) {
                    // 要求送达时间
                    LocalDateTime requireArrivalTime = LocalDateTime.parse(handleWayBill.getGyRequireArrivalTime(),
                            DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
                    // 预计送达时间
                    LocalDateTime predictArrivalTime = LocalDateTime.parse(handleWayBill.getPredictArrivalTime(),
                            DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
                    Long minutes = ChronoUnit.MINUTES.between(predictArrivalTime, requireArrivalTime);
                    if (minutes <= validTime) {// 预警
                        countMap.put(Globals.ONE_WAYBILL_WARN_COUNT, countMap.get(Globals.ONE_WAYBILL_WARN_COUNT) + 1);
                        updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
                        this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.ARRIVAL_WARNING,
                                minutes, now, AlarmWarning.WARNING);
                    }
                }
            }
        }
        return countMap;
    }

    /**
     * 回单时效预报警
     *
     * @param etWaybillExceptionInsertList
     * @param handleWayBill
     * @param updateWayBillNos
     * @param alertRuleItemList
     * @param now
     * @param countMap
     * @return
     */
    private Map<String, Integer> packageReceiptWarning(List<EtWaybillException> etWaybillExceptionInsertList, HandleWayBill handleWayBill,
            List<String> updateWayBillNos, List<MonAlertRuleItem> alertRuleItemList, LocalDateTime now, Map<String, Integer> countMap) {
        logger.info("开始执行回单时效预报警");
        // 如果已回单报警过，直接返回
        if (handleWayBill.getIsReceiptAlarm() != null && YesNo.YES.getUpperKey().equals(handleWayBill.getIsReceiptAlarm())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已回单报警过，直接返回");
            return countMap;
        }
        if (StringUtils.isEmpty(handleWayBill.getGyRequireReceiptUploadTime())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，无要求回单时间，直接返回");
            return countMap;
        }
        // 回单超时：要求回单时间到达后还未进行回单上传，或实际回单时间晚于要求回单时间，则进行报警
        if ((handleWayBill.getHasReceipt().intValue() == 1 && DateJDK8SafeUtil
                .dateTimeFromIsAfterDateTimeTo(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)),
                        handleWayBill.getGyRequireReceiptUploadTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (
                handleWayBill.getHasReceipt().intValue() == 0 && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(handleWayBill.getActReceiptTime(), handleWayBill.getGyRequireReceiptUploadTime(),
                                DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))) {
            countMap.put(Globals.ONE_WAYBILL_OVER_TIME_COUNT, countMap.get(Globals.ONE_WAYBILL_OVER_TIME_COUNT) + 1);
            updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
            this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.RECEIPT_WARNING, null, now,
                    AlarmWarning.ALARM);
        } else {
            if (!CollectionUtils.isEmpty(alertRuleItemList)) {
                if (handleWayBill.getIsReceiptEarlyWarning() != null && YesNo.YES.getUpperKey()
                        .equals(handleWayBill.getIsReceiptEarlyWarning())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已回单预警过，直接返回");
                    return countMap;
                }
                // 回单预警：要求回单时间前m分钟内还未进行回单，则进行预警
                LocalDateTime estimatedUploadTime = LocalDateTime.parse(handleWayBill.getGyRequireReceiptUploadTime(),
                        DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
                Long minutes = ChronoUnit.MINUTES.between(now, estimatedUploadTime);
                if (com.sinoservices.common.other.EmptyUtils.isNotEmpty(alertRuleItemList.get(0).getRuleItemValue())) {
                    Integer validTime = Integer.parseInt(alertRuleItemList.get(0).getRuleItemValue());
                    if (handleWayBill.getActReceiptTime() == null && minutes <= validTime) {// 预警
                        countMap.put(Globals.ONE_WAYBILL_WARN_COUNT, countMap.get(Globals.ONE_WAYBILL_WARN_COUNT) + 1);
                        updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
                        this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.RECEIPT_WARNING,
                                minutes, now, AlarmWarning.WARNING);
                    }
                }
            }
        }
        return countMap;
    }

    /**
     * 返单时效预报警
     *
     * @param etWaybillExceptionInsertList
     * @param handleWayBill
     * @param updateWayBillNos
     * @param alertRuleItemList
     * @param now
     * @param countMap
     * @return
     */
    private Map<String, Integer> packageReturnWarning(List<EtWaybillException> etWaybillExceptionInsertList, HandleWayBill handleWayBill,
            List<String> updateWayBillNos, List<MonAlertRuleItem> alertRuleItemList, LocalDateTime now, Map<String, Integer> countMap) {
        logger.info("开始执行返单时效预报警");
        // 如果已返单报警过，直接返回
        if (handleWayBill.getIsReturnAlarm() != null && YesNo.YES.getUpperKey().equals(handleWayBill.getIsReturnAlarm())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已返单报警过，直接返回");
            return countMap;
        }
        if (StringUtils.isEmpty(handleWayBill.getGyRequireReceiptReturnTime())) {
            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，无要求返单时间，直接返回");
            return countMap;
        }
        // 返单超时：要求返单时间到达后还未进行返单，或实际返单时间晚于要求返单时间，则进行报警
        if ((handleWayBill.getActReturnTime() == null && DateJDK8SafeUtil
                .dateTimeFromIsAfterDateTimeTo(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)),
                        handleWayBill.getGyRequireReceiptReturnTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (
                handleWayBill.getActReturnTime() != null && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(handleWayBill.getActReturnTime(), handleWayBill.getGyRequireReceiptReturnTime(),
                                DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))) {
            countMap.put(Globals.ONE_WAYBILL_OVER_TIME_COUNT, countMap.get(Globals.ONE_WAYBILL_OVER_TIME_COUNT) + 1);
            updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
            this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.RETURN_WARNING, null, now,
                    AlarmWarning.ALARM);
        } else {
            if (!CollectionUtils.isEmpty(alertRuleItemList)) {
                if (handleWayBill.getIsReturnEarlyWarning() != null && YesNo.YES.getUpperKey()
                        .equals(handleWayBill.getIsReturnEarlyWarning())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，已返单预警过，直接返回");
                    return countMap;
                } // 要求返单时间-当前时间<=提前预警时长
                LocalDateTime expectedOriginalReturnTime = LocalDateTime.parse(handleWayBill.getGyRequireReceiptReturnTime(),
                        DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
                Long minutes = ChronoUnit.MINUTES.between(now, expectedOriginalReturnTime);
                if (com.sinoservices.common.other.EmptyUtils.isNotEmpty(alertRuleItemList.get(0).getRuleItemValue())) {
                    Integer validTime = Integer.parseInt(alertRuleItemList.get(0).getRuleItemValue());
                    if (handleWayBill.getActReturnTime() == null && minutes <= validTime) {// 预警
                        countMap.put(Globals.ONE_WAYBILL_WARN_COUNT, countMap.get(Globals.ONE_WAYBILL_WARN_COUNT) + 1);
                        updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
                        this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.RETURN_WARNING,
                                minutes, now, AlarmWarning.WARNING);
                    }
                }
            }
        }
        return countMap;
    }

    /**
     * 退货时效预报警
     *
     * @param etWaybillExceptionInsertList
     * @param handleWayBill
     * @param updateWayBillNos
     * @param alertRuleItemList
     * @param now
     * @param countMap
     * @return
     */
    private Map<String, Integer> packageReturnGoodsWarning(List<EtWaybillException> etWaybillExceptionInsertList,
            HandleWayBill handleWayBill, List<String> updateWayBillNos, List<MonAlertRuleItem> alertRuleItemList, LocalDateTime now,
            List<EtWaybillExceptionDto> etWaybillExceptionDtoUpdateList, Map<String, Integer> countMap) {
        logger.info("开始执行退货时效预报警");
        EtWaybillExceptionCondition condition = new EtWaybillExceptionCondition();
        condition.setWaybillNo(handleWayBill.getOwbWayBillNo());
        List<EtWaybillExceptionDto> etWaybillExceptionDtoList = etWaybillExceptionBiz.findReturnList(condition);
        if (!CollectionUtils.isEmpty(etWaybillExceptionDtoList)) {
            for (EtWaybillExceptionDto etWaybillExceptionDto : etWaybillExceptionDtoList) {
                // 如果已退货报警过，直接返回
                if (etWaybillExceptionDto.getIsReturnGoodsAlarm() != null && YesNo.YES.getUpperKey()
                        .equals(etWaybillExceptionDto.getIsReturnGoodsAlarm())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，异常编号：[" + etWaybillExceptionDto.getExceptionNo()
                            + "]，已退货报警过，直接返回");
                    return countMap;
                }
                if (StringUtils.isEmpty(handleWayBill.getGyRequireReturnTime())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，无要求退货时间，直接返回");
                    return countMap;
                }
                // 退货超时：要求退货时间到达后还未进行退货，或实际退货时间晚于要求退货时间，则进行报警（2018-11-05 todd确认：先以异常审核时间作为实际退货时间）
                if ((etWaybillExceptionDto.getAuditTime() == null && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)),
                                handleWayBill.getGyRequireReturnTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (
                        etWaybillExceptionDto.getAuditTime() != null && DateJDK8SafeUtil
                                .dateTimeFromIsAfterDateTimeTo(etWaybillExceptionDto.getAuditTime(), handleWayBill.getGyRequireReturnTime(),
                                        DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))) {
                    countMap.put(Globals.ONE_WAYBILL_OVER_TIME_COUNT, countMap.get(Globals.ONE_WAYBILL_OVER_TIME_COUNT) + 1);
                    updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
                    this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill, AlertRuleTypeEnum.RETURN_GOODS_WARNING,
                            null, now, AlarmWarning.ALARM);
                    etWaybillExceptionDto.setIsReturnGoodsAlarm(YesNo.YES.getUpperKey());
                    etWaybillExceptionDtoUpdateList.add(etWaybillExceptionDto);
                } else {
                    if (!CollectionUtils.isEmpty(alertRuleItemList)) {
                        if (etWaybillExceptionDto.getIsReturnGoodsEarlyWarning() != null && YesNo.YES.getUpperKey()
                                .equals(etWaybillExceptionDto.getIsReturnGoodsEarlyWarning())) {
                            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，异常编号：[" + etWaybillExceptionDto.getExceptionNo()
                                    + "]，已退货预警过，直接返回");
                            return countMap;
                        } // 要求退货时间-当前时间<=提前预警时长
                        LocalDateTime expectedOriginalReturnTime = LocalDateTime.parse(handleWayBill.getGyRequireReturnTime(),
                                DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
                        Long minutes = ChronoUnit.MINUTES.between(now, expectedOriginalReturnTime);
                        if (EmptyUtils.isNotEmpty(alertRuleItemList.get(0).getRuleItemValue())) {
                            Integer validTime = Integer.parseInt(alertRuleItemList.get(0).getRuleItemValue());
                            if (etWaybillExceptionDto.getAuditTime() == null && minutes <= validTime) {// 预警
                                countMap.put(Globals.ONE_WAYBILL_WARN_COUNT, countMap.get(Globals.ONE_WAYBILL_WARN_COUNT) + 1);
                                updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
                                this.packageExcetionData(etWaybillExceptionInsertList, null, handleWayBill,
                                        AlertRuleTypeEnum.RETURN_GOODS_WARNING, minutes, now, AlarmWarning.WARNING);
                                etWaybillExceptionDto.setIsReturnGoodsEarlyWarning(YesNo.YES.getUpperKey());
                                etWaybillExceptionDtoUpdateList.add(etWaybillExceptionDto);
                            }
                        }
                    }
                }
            }
        }
        return countMap;
    }

    /**
     * 温度计返回时效预报警
     *
     * @param etWaybillExceptionInsertList
     * @param handleWayBill
     * @param updateWayBillNos
     * @param alertRuleItemList
     * @param now
     * @param countMap
     * @return
     */
    private Map<String, Integer> packageThermometerReturnWarning(List<EtWaybillException> etWaybillExceptionInsertList,
            HandleWayBill handleWayBill, List<String> updateWayBillNos, List<MonAlertRuleItem> alertRuleItemList, LocalDateTime now,
            List<GyThermometerHistory> thermometerHistoryUpdateList, Map<String, Integer> countMap) {
        logger.info("开始执行温度计返回时效预报警");
        GyThermometerHistoryCondition condition = new GyThermometerHistoryCondition();
        condition.setWaybillNo(handleWayBill.getOwbWayBillNo());
        List<GyThermometerHistory> gyThermometerHistoryList = gyThermometerHistoryBiz.findList(condition);
        if (!CollectionUtils.isEmpty(gyThermometerHistoryList)) {
            for (GyThermometerHistory gyThermometerHistory : gyThermometerHistoryList) {
                // 如果已温度计返回报警过，直接返回
                if (gyThermometerHistory.getIsThermometerAlarm() != null && YesNo.YES.getUpperKey()
                        .equals(gyThermometerHistory.getIsThermometerAlarm())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，温度计编号：[" + gyThermometerHistory.getThermometerNo()
                            + "]，已温度计返回报警过，直接返回");
                    return countMap;
                }
                if (StringUtils.isEmpty(handleWayBill.getGyRequireThermometerReturnTime())) {
                    logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，无要求温度计返回时间，直接返回");
                    return countMap;
                }

                // 温度计返回超时：要求温度计返回时间到达后还未进行返回，或实际返回时间晚于要求返回时间，则进行报警
                if ((gyThermometerHistory.getTemperatureReceiptTime() == null && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(now.format(DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)),
                                handleWayBill.getGyRequireThermometerReturnTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) || (
                        gyThermometerHistory.getTemperatureReceiptTime() != null && DateJDK8SafeUtil
                                .dateTimeFromIsAfterDateTimeTo(gyThermometerHistory.getTemperatureReceiptTime(),
                                        handleWayBill.getGyRequireThermometerReturnTime(), DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))) {
                    countMap.put(Globals.ONE_WAYBILL_OVER_TIME_COUNT, countMap.get(Globals.ONE_WAYBILL_OVER_TIME_COUNT) + 1);
                    updateWayBillNos.add(handleWayBill.getOwbWayBillNo());

                    this.packageExcetionData(etWaybillExceptionInsertList, gyThermometerHistory.getThermometerNo(), handleWayBill,
                            AlertRuleTypeEnum.THERMOMETER_RETURN_WARNING, null, now, AlarmWarning.ALARM);
                    gyThermometerHistory.setIsThermometerAlarm(YesNo.YES.getUpperKey());
                    thermometerHistoryUpdateList.add(gyThermometerHistory);
                } else {
                    if (!CollectionUtils.isEmpty(alertRuleItemList)) {
                        if (gyThermometerHistory.getIsThermometerEarlyWarning() != null && YesNo.YES.getUpperKey()
                                .equals(gyThermometerHistory.getIsThermometerEarlyWarning())) {
                            logger.info("运单：[" + handleWayBill.getOwbWayBillNo() + "]，温度计编号：[" + gyThermometerHistory.getThermometerNo()
                                    + "]，，已温度计返回预警过，直接返回");
                            return countMap;
                        } // 要求温度计返回时间-当前时间<=提前预警时长
                        LocalDateTime expectedOriginalReturnTime = LocalDateTime.parse(handleWayBill.getGyRequireThermometerReturnTime(),
                                DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS));
                        Long minutes = ChronoUnit.MINUTES.between(now, expectedOriginalReturnTime);
                        if (EmptyUtils.isNotEmpty(alertRuleItemList.get(0).getRuleItemValue())) {
                            Integer validTime = Integer.parseInt(alertRuleItemList.get(0).getRuleItemValue());
                            if (gyThermometerHistory.getTemperatureReceiptTime() == null && minutes <= validTime) {// 预警
                                countMap.put(Globals.ONE_WAYBILL_WARN_COUNT, countMap.get(Globals.ONE_WAYBILL_WARN_COUNT) + 1);
                                updateWayBillNos.add(handleWayBill.getOwbWayBillNo());
                                this.packageExcetionData(etWaybillExceptionInsertList, gyThermometerHistory.getThermometerNo(),
                                        handleWayBill, AlertRuleTypeEnum.THERMOMETER_RETURN_WARNING, minutes, now, AlarmWarning.WARNING);
                                gyThermometerHistory.setIsThermometerEarlyWarning(YesNo.YES.getUpperKey());
                                thermometerHistoryUpdateList.add(gyThermometerHistory);
                            }
                        }
                    }
                }
            }
        }
        return countMap;
    }
}
