package com.jlt.datasync.timer;

import com.jlt.common.constant.Globals;
import com.jlt.datasync.biz.EtWaybillExceptionBiz;
import com.jlt.datasync.biz.GyAlertMessageBiz;
import com.jlt.datasync.biz.GyjlDispatchOrderBiz;
import com.jlt.datasync.biz.HadleWayBillBiz;
import com.jlt.datasync.condition.EtWaybillExceptionCondition;
import com.jlt.datasync.condition.GyAlertMessageCondition;
import com.jlt.datasync.condition.HandleWayBillCondition;
import com.jlt.datasync.domain.GyAlertMessage;
import com.jlt.datasync.domain.GyAlertRule;
import com.jlt.datasync.domain.GyjlDispatchOrder;
import com.jlt.datasync.domain.HandleWayBill;
import com.jlt.datasync.dto.EtWaybillExceptionDto;
import com.sinoservices.common.date.DateJDK8SafeUtil;
import com.sinoservices.common.util.IDGenerator;
import com.sinoservices.minima.common.util.EmptyUtil;
import com.sinoservices.xframework.core.utils.BeanUtils;
import com.sinoservices.xframework.core.utils.EmptyUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 国药定时器抽取今天之前3天数据，组成跟单平台
 *
 * @author Billy.Zhang
 */
@Component
@JobHander("gy:dispatchorder")
public class GyDispatchOrderHandleJobTask extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GyjlDispatchOrderBiz gyjlDispatchOrderManager;
    @Autowired
    private HadleWayBillBiz billManager;
    @Autowired
    private EtWaybillExceptionBiz exceptionManager;
    @Autowired
    private GyAlertMessageBiz messageManager;


    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("开始执行派车单生成，定时任务");
        dataHandleTaskEntrance();
        logger.info("派车单生成定时任务，执行成功");
        return ReturnT.SUCCESS;
    }

    /**
     * 处理数据的入口
     */
    private void dataHandleTaskEntrance() {
        // 封装调度单号与运单温度返回情况map集合
        Map<String, List<Integer>> owbEcNoMap = new HashMap<>();
        String endDate = DateJDK8SafeUtil.getNowDate() + " 23:59:59";
        String startDate = DateJDK8SafeUtil.getPreviousDateByDays(15) + " 00:00:00";
        getWaybillData(startDate, endDate, owbEcNoMap);
    }


    /**
     * 获取运单数据，时间今天之前3天
     *
     * @param startTime
     * @param endTime
     */
    private void getWaybillData(String startTime, String endTime, Map<String, List<Integer>> owbEcNoMap) {
        HandleWayBillCondition billCondition = new HandleWayBillCondition();
        billCondition.setStartTime(startTime);
        billCondition.setEndTime(endTime);
        List<HandleWayBill> billModels = billManager.getWayBillWithByCondition(billCondition);
        logger.info("获取运单，日期：(" + startTime + "=>" + endTime + ")，获取记录数：" + (EmptyUtils.isEmpty(billModels) ? 0 : billModels.size()));
        if (EmptyUtils.isEmpty(billModels)) {
            return;
        }
        // 获取运单号集合
        List<String> waybillNos = billModels.stream().map(HandleWayBill::getOwbWayBillNo).collect(Collectors.toList());
        // 封装调度单与与温度计返回情况 map<调度单号,温度计返回情况>
        generateDispatchNoReturnTemperatureMap(billModels, owbEcNoMap);
        // 查询异常处理情况
        EtWaybillExceptionCondition eoOrderWayBillExceptionCondition = new EtWaybillExceptionCondition();
        eoOrderWayBillExceptionCondition.setStartTime(startTime);
        eoOrderWayBillExceptionCondition.setEndTime(endTime);
        eoOrderWayBillExceptionCondition.setWaybillNos(waybillNos);
        List<EtWaybillExceptionDto> billExceptionViews = exceptionManager.getExceptionByCondition(eoOrderWayBillExceptionCondition);

        // 查询运单事件执行情况
        HandleWayBillCondition eventCondition = new HandleWayBillCondition();
        eventCondition.setWaybillNos(waybillNos);
        List<HandleWayBill> eventInfos = billManager.getWayBillEventByCondition(eventCondition);
        // 封装调度单数据
        packageDispatchData(billModels, billExceptionViews, eventInfos, owbEcNoMap);
        // 增加异常提醒信息
        packageExceptionMessage(billExceptionViews, billModels);

        // 查询运单事件执行情况 (派车调度-->已调度 /提货到场-->车到场 /提货确认-->在途 /运抵-->已运抵/ 正常签收、异常签收-->已签收/ 回单上传-->已上传/
        // 纸质返单确认-->已返单（跟单平台特有，tms暂无）（二期返单确认后加）异常提货-->在途)
        HandleWayBillCondition eventConditionForUpdateStatus = new HandleWayBillCondition();
        eventConditionForUpdateStatus.setWaybillNos(waybillNos);
        List<String> operationTypes = new ArrayList<>();
        operationTypes.add("30");// 派车调度-->已调度
        operationTypes.add("220");// 提货到场-->车到场
        operationTypes.add("130");// 提货确认-->在途
        // operationTypes.add("270");// 运抵-->已运抵
        operationTypes.add("180");// 运抵确认-->已运抵
        operationTypes.add("190");// 正常签收-->已签收
        operationTypes.add("200");// 异常签收-->已签收
        operationTypes.add("210");// 回单上传-->已上传
        operationTypes.add("140");// 异常提货-->在途
        eventConditionForUpdateStatus.setOperationTypes(operationTypes);
        List<HandleWayBill> afterFilteringEventInfos = billManager.getWayBillEventByCondition(eventConditionForUpdateStatus);
        // 封装运单数据，更新国药运单状态字段
        packageWayBillData(billModels, afterFilteringEventInfos);

    }

    /**
     * 封装运单数据，更新国药运单状态字段
     */
    private void packageWayBillData(List<HandleWayBill> billModels, List<HandleWayBill> afterFilteringEventInfos) {
        Map<String, HandleWayBill> eventMap = new HashMap<>();
        for (HandleWayBill eventInfo : afterFilteringEventInfos) {
            if (null == eventMap.get(eventInfo.getOwbWayBillNo())) {
                eventMap.put(eventInfo.getOwbWayBillNo(), eventInfo);
            } else {
                HandleWayBill a = eventMap.get(eventInfo.getOwbWayBillNo());
                if (EmptyUtils.isNotEmpty(eventInfo.getOperationTime()) && EmptyUtils.isNotEmpty(a.getOperationTime()) && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(eventInfo.getOperationTime().substring(0, 19), a.getOperationTime().substring(0, 19),
                                DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) {
                    eventMap.put(eventInfo.getOwbWayBillNo(), eventInfo);
                }
            }
        }

        packageWayBillExtUpdate(billModels, eventMap);
    }

    /**
     * 更新国药运单状态字段 (派车调度-->已调度 /提货到场-->车到场 /提货确认-->在途 /运抵-->已运抵/ 正常签收、异常签收-->已签收/ 回单上传-->已上传/ 纸质返单确认-->已返单（跟单平台特有，tms暂无）（二期返单确认后加）)
     *
     * @param billModels
     * @param eventMap   operationTypes.add("30");// 派车调度-->已调度 operationTypes.add("220");// 提货到场-->车到场 operationTypes.add("130");// 提货确认-->在途
     *                   operationTypes.add("270");// 运抵-->已运抵 operationTypes.add("190");// 正常签收-->已签收 operationTypes.add("200");// 异常签收-->已签收
     *                   operationTypes.add("210");// 回单上传-->已上传
     */
    private void packageWayBillExtUpdate(List<HandleWayBill> billModels, Map<String, HandleWayBill> eventMap) {
        List<HandleWayBill> udpateList = new ArrayList<>();
        for (HandleWayBill model : billModels) {
            if (eventMap.get(model.getOwbWayBillNo()) != null) {
                HandleWayBill saveModel = new HandleWayBill();
                saveModel.setOwbWayBillNo(model.getOwbWayBillNo());
                switch (eventMap.get(model.getOwbWayBillNo()).getOperationType()) {
                    case "30":
                        saveModel.setGyWaybillStatus("已调度");
                        break;
                    case "220":
                        saveModel.setGyWaybillStatus("车到场");
                        break;
                    case "130":
                        saveModel.setGyWaybillStatus("在途");
                        break;
                    case "140":
                        saveModel.setGyWaybillStatus("在途");
                        break;
                    case "180":
                        saveModel.setGyWaybillStatus("已运抵");
                        break;
                    case "190":
                        saveModel.setGyWaybillStatus("已签收");
                        break;
                    case "200":
                        saveModel.setGyWaybillStatus("已签收");
                        break;
                    case "210":
                        saveModel.setGyWaybillStatus("已上传");
                        break;
                }
                udpateList.add(saveModel);
            }
        }
        if (EmptyUtils.isNotEmpty(udpateList)) {
            billManager.batchUpdateWaybillInfoExt(udpateList);
        }
    }

    /**
     * 抽取异常提示信息
     *
     * @param billExceptionViews
     */
    private void packageExceptionMessage(List<EtWaybillExceptionDto> billExceptionViews, List<HandleWayBill> billModels) {
        Integer ZREO = 0;
        List<String> waybillNos = new ArrayList<>();
        Map<String, Set<String>> exceptionMap = new HashMap<>();
        for (EtWaybillExceptionDto exceptionView : billExceptionViews) {
            if (ZREO.equals(exceptionView.getGyHandleFlag())) {
                waybillNos.add(exceptionView.getWaybillNo());
                if (exceptionMap.containsKey(exceptionView.getWaybillNo())) {
                    exceptionMap.get(exceptionView.getWaybillNo()).add(exceptionView.getExceptionBroadHeading());
                } else {
                    Set<String> exceptionSet = new TreeSet<>();
                    exceptionSet.add(exceptionView.getExceptionBroadHeading());
                    exceptionMap.put(exceptionView.getWaybillNo(), exceptionSet);
                }
            }
        }

        Map<Long, Set<String>> messageMap = new HashMap<>();
        Set<Long> orgSet = new TreeSet<>();
        for (HandleWayBill wayBillModel : billModels) {
            if (waybillNos.contains(wayBillModel.getOwbWayBillNo())) {
                orgSet.add(wayBillModel.getTenantId());
                if (exceptionMap.containsKey(wayBillModel.getOwbWayBillNo())) {
                    if (messageMap.containsKey(wayBillModel.getTenantId())) {
                        messageMap.get(wayBillModel.getTenantId()).addAll(exceptionMap.get(wayBillModel.getOwbWayBillNo()));
                    } else {
                        messageMap.put(wayBillModel.getTenantId(), exceptionMap.get(wayBillModel.getOwbWayBillNo()));
                    }
                }
            }
        }
        List<Long> tenantIdList = new ArrayList<>(orgSet);
        List<GyAlertMessage> models = new ArrayList<>();
        String time = DateJDK8SafeUtil.getNowDateTime();
        for (Long tenantId : tenantIdList) {
            GyAlertMessage model = new GyAlertMessage();
            model.setId(IDGenerator.getLongId());
            StringBuilder exceptionMessage = new StringBuilder();
            if (EmptyUtils.isNotEmpty(messageMap.get(tenantId))) {
                messageMap.get(tenantId).stream().forEach(exceptiontype -> exceptionMessage.append(exceptiontype).append("，"));
            }
            model.setAlertMessage("您绑定的承运商下有异常发生，包括：" + exceptionMessage.toString() + "请及时处理！");
            model.setAlertTime(time);
            model.setTenantId(tenantId);
            model.setCreateName("定时器");
            model.setCreator("定时器");
            models.add(model);
        }
        // 先删除，再新增
        GyAlertMessageCondition messageCondition = new GyAlertMessageCondition();
        messageManager.deleteByCondition(messageCondition);
        if (EmptyUtils.isNotEmpty(models)) {
            messageManager.batchInsert(models);
        }
    }

    /**
     * 封装调度单数据
     *
     * @param billModels
     * @param billExceptionViews
     * @param owbEcNoMap
     */
    private void packageDispatchData(List<HandleWayBill> billModels, List<EtWaybillExceptionDto> billExceptionViews,
            List<HandleWayBill> eventInfos, Map<String, List<Integer>> owbEcNoMap) {
        List<GyjlDispatchOrder> saveModelList = new ArrayList<>();
        Set<String> ecnoSet = new HashSet<>();
        //抽取派车单号集合（有外部取外部，没外部取内部）
        for (HandleWayBill handleWayBill : billModels) {
            ecnoSet.add(EmptyUtil.isNotEmpty(handleWayBill.getExEcNo()) ? handleWayBill.getExEcNo() : handleWayBill.getOwbEcNo());
        }
        List<String> ecNoList = new ArrayList<>(ecnoSet);
        if (EmptyUtils.isNotEmpty(ecNoList)) {
            for (String owbEcNo : ecNoList) {
                GyjlDispatchOrder model = packageWayBillData(billModels, owbEcNo, eventInfos, billExceptionViews, owbEcNoMap);
                saveModelList.add(model);
            }
        }
        if (EmptyUtils.isNotEmpty(saveModelList)) {
            logger.info("Merge派车单");
            gyjlDispatchOrderManager.batchMerge(saveModelList);
        }
    }

    /**
     * 封装运单数据
     *
     * @param billModels
     * @param owbEcNo
     */
    private GyjlDispatchOrder packageWayBillData(List<HandleWayBill> billModels, String owbEcNo, List<HandleWayBill> eventInfos,
            List<EtWaybillExceptionDto> billExceptionViews, Map<String, List<Integer>> owbEcNoMap) {
        GyjlDispatchOrder saveModel = new GyjlDispatchOrder();
        Predicate<HandleWayBill> ecNoFilter =
                model -> (owbEcNo.equals(model.getOwbEcNo()) || (model.getExEcNo() != null && owbEcNo.equals(model.getExEcNo())));
        List<HandleWayBill> filterModels = billModels.stream().filter(ecNoFilter).collect(Collectors.toList());
        BeanUtils.copyProperties(saveModel, filterModels.get(0));
        saveModel.setId(IDGenerator.getLongId());
        if (EmptyUtil.isNotEmpty(saveModel.getExEcNo())) {
            saveModel.setOwbEcNo(saveModel.getExEcNo());
        }
        saveModel.setDispatchTime(filterModels.get(0).getDispatchTime());
        List<Integer> returnTemperatures = owbEcNoMap.get(owbEcNo);
        if (EmptyUtils.isNotEmpty(returnTemperatures)) {
            if (!returnTemperatures.contains(10) && !returnTemperatures.contains(40)) {
                saveModel.setReturnTemperature(30);
            } else if (!returnTemperatures.contains(20) && !returnTemperatures.contains(30) && !returnTemperatures.contains(40)
                    && !returnTemperatures.contains(50)) {
                saveModel.setReturnTemperature(10);
            } else if (returnTemperatures.contains(40)) {
                saveModel.setReturnTemperature(20);
            }
        }

        saveModel.setReturnReceipt(10);
        // 调度单状态和执行状态提前赋值
        saveModel.setTrackStatus(30);
        saveModel.setDispatchStatus(10);// 默认待提货
        saveModel.setCreateName("接口");
        saveModel.setCreator("接口");

        //根据派车单号，查询有效的运单数
        Integer count = billManager.getValidWaybill(saveModel.getOwbEcNo());
        if (count == 0) {
            saveModel.setRecStatus(1);
        } else {
            saveModel.setRecStatus(0);
        }

        List<String> waybillNoList = filterModels.stream().map(HandleWayBill::getOwbWayBillNo).collect(Collectors.toList());
        List<EtWaybillExceptionDto> exceptionViews = filterWayBillException(waybillNoList, billExceptionViews);
        List<HandleWayBill> eventFilters = filterWayBillEvent(waybillNoList, eventInfos);
        fillDispatchData(saveModel, exceptionViews, filterModels);
        fillTrackEventNode(filterModels, saveModel, eventFilters, waybillNoList.size());
        fillDispatchStatus(filterModels, saveModel, eventFilters, waybillNoList.size());

        return saveModel;
    }

    private List<HandleWayBill> filterWayBillEvent(List<String> waybillNoList, List<HandleWayBill> eventInfos) {
        Predicate<HandleWayBill> waybillNoFilter = model -> waybillNoList.contains(model.getOwbWayBillNo());
        return eventInfos.stream().filter(waybillNoFilter).collect(Collectors.toList());
    }

    /**
     * 抽取异常数据
     *
     * @param waybillNoList
     * @param billExceptionViews
     * @return
     */
    private List<EtWaybillExceptionDto> filterWayBillException(List<String> waybillNoList, List<EtWaybillExceptionDto> billExceptionViews) {
        Predicate<EtWaybillExceptionDto> pmCodeFilter = model -> waybillNoList.contains(model.getWaybillNo());
        return billExceptionViews.stream().filter(pmCodeFilter).collect(Collectors.toList());
    }

    /**
     * 填充计算数据
     *
     * @param saveModel
     * @param exceptionViews
     * @param filterModels
     */
    private void fillDispatchData(GyjlDispatchOrder saveModel, List<EtWaybillExceptionDto> exceptionViews,
            List<HandleWayBill> filterModels) {
        Integer allCount = filterModels.size();
        Integer signCount = 0;
        Integer hadnleCount = 0;
        Integer unHandleCount = 0;
        Integer isExceptionCount = 0;
        for (HandleWayBill handleWayBillModel : filterModels) {
            if (handleWayBillModel.getWaybillStatus() != null && handleWayBillModel.getWaybillStatus() == 50) {
                signCount += 1;
            }
            if ("0".equals(handleWayBillModel.getOwbHasGps()) || "0".equals(handleWayBillModel.getOwbHasApp())) {
                saveModel.setHasGps(0);
            }
            if ("0".equals(handleWayBillModel.getOwbHasTemperature())) {
                saveModel.setHasTemperture(0);
            }
        }

        Integer ZREO = 0;
        Integer ONE = 1;
        TreeSet<String> exceptionNameSet = new TreeSet<>();
        for (EtWaybillExceptionDto exceptionView : exceptionViews) {
            exceptionNameSet.add(exceptionView.getExceptionBroadHeading());
            if (ZREO.equals(exceptionView.getGyHandleFlag())) {
                unHandleCount += 1;
            }
            if (ONE.equals(exceptionView.getGyHandleFlag())) {
                hadnleCount += 1;
            }
            if (ZREO.equals(exceptionView.getIsException())) {
                isExceptionCount += 1;
            }
        }
        if (!exceptionNameSet.isEmpty()) {
            StringBuilder message = new StringBuilder("请注意，当前派车单下有");
            message.append(exceptionViews.size()).append("笔异常记录，包括：").append(transformateException(exceptionNameSet)).append("，其中未处理：")
                    .append(unHandleCount).append("条，已处理：").append(hadnleCount).append("条。");

            saveModel.setExceptionMessage(message.toString());
            if (unHandleCount > 0) {
                saveModel.setHandleStatus(10);
            } else {
                saveModel.setHandleStatus(20);
            }
        }
        if (isExceptionCount == 0) {
            saveModel.setExceptionStatus(10);
        } else {
            saveModel.setExceptionStatus(EmptyUtils.isNotEmpty(exceptionViews) ? 20 : 10);
            if (20 == saveModel.getTrackStatus() || 10 == saveModel.getTrackStatus()) {
                saveModel.setExceptionStatus(20);
            }
        }
        double progre = signCount / (allCount * 1.0);
        BigDecimal sum = BigDecimal.valueOf(progre * 100);
        saveModel.setDispatchProgress(sum.intValue());
    }

    /**
     * 填充调度单状态
     *
     * @param saveModel
     * @param eventFilters
     */
    private void fillDispatchStatus(List<HandleWayBill> billModels, GyjlDispatchOrder saveModel, List<HandleWayBill> eventFilters,
            Integer waybillCount) {
        Map<String, HandleWayBill> eventMap = new HashMap<>();
        for (HandleWayBill eventInfo : eventFilters) {
            if (null == eventMap.get(eventInfo.getOwbWayBillNo())) {
                eventMap.put(eventInfo.getOwbWayBillNo(), eventInfo);
            } else {
                HandleWayBill a = eventMap.get(eventInfo.getOwbWayBillNo());
                if (EmptyUtils.isNotEmpty(eventInfo.getOperationTime()) && EmptyUtils.isNotEmpty(a.getOperationTime()) && DateJDK8SafeUtil
                        .dateTimeFromIsAfterDateTimeTo(eventInfo.getOperationTime().substring(0, 19), a.getOperationTime().substring(0, 19),
                                DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS)) {
                    eventMap.put(eventInfo.getOwbWayBillNo(), eventInfo);
                }
            }
        }

        List<HandleWayBill> handleEventFilters = new ArrayList();
        Iterator<Entry<String, HandleWayBill>> iter = eventMap.entrySet().iterator(); // 获得map的Iterator
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            handleEventFilters.add((HandleWayBill) entry.getValue());
        }

        Integer pickArriver = 0;
        Integer signArriver = 0;
        Integer signCount = 0;
        Integer truckCount = 0;
        Integer dispatchCount = 0;
        Integer receiptCount = 0;
        for (HandleWayBill eventModel : handleEventFilters) {
            if ("30".equals(eventModel.getOperationType())) {// 派车调度:30
                dispatchCount += 1;
            }
            if ("220".equals(eventModel.getOperationType())) {// 提货到场:220
                pickArriver += 1;
            }
            if ("130".equals(eventModel.getOperationType()) || "140".equals(eventModel.getOperationType()) || "150"
                    .equals(eventModel.getOperationType()) || "160".equals(eventModel.getOperationType()) || "170"
                    .equals(eventModel.getOperationType())) {// 在途
                truckCount += 1;
            }
            if ("270".equals(eventModel.getOperationType()) || "180".equals(eventModel.getOperationType())) {// 运抵:270，运抵确认：180
                signArriver += 1;
            }
            if ("190".equals(eventModel.getOperationType()) || "200".equals(eventModel.getOperationType())) {// 正常签收:190, 异常签收:200
                signCount += 1;
            }
            if ("210".equals(eventModel.getOperationType())) {// 回单上传:210
                receiptCount += 1;
            }
        }
        /*logger.info(
                "exEcNo:" + saveModel.getExEcNo() + ",waybillCount:" + waybillCount + ",dispatchCount:" + dispatchCount + ",pickArriver:"
                        + pickArriver + ",truckCount:" + truckCount + ",signArriver:" + signArriver + ",signCount:" + signCount
                        + ",receiptCount:" + receiptCount);*/

        if (dispatchCount == waybillCount) {
            // 所有派车调度
            saveModel.setDispatchStatus(10);// 待提货
        }
        if (pickArriver == waybillCount) {
            // 所有提货到场或提货确认
            saveModel.setDispatchStatus(20); // 提货到场
        }

        if (truckCount == waybillCount) {
            saveModel.setDispatchStatus(30);// 在途
        }

        if (signArriver == waybillCount || signCount == waybillCount) {
            // 所有运抵或签收
            saveModel.setDispatchStatus(40); // 抵达
        }

        if (signCount == waybillCount) {
            // 所有签收变成签收
            saveModel.setDispatchStatus(50); // 签收
        }

        if (receiptCount == waybillCount) {
            // 所有回单上传
            saveModel.setDispatchStatus(60);// 回单
        }
    }

    private void fillTrackEventNode(List<HandleWayBill> billModels, GyjlDispatchOrder saveModel, List<HandleWayBill> eventFilters,
            Integer waybillCount) {
        Integer pickArriver = 0;
        Integer signArriver = 0;
        Map<String, String> eventMap = new HashMap<>();
        for (HandleWayBill eventModel : eventFilters) {

            if ("220".equals(eventModel.getOperationType())) {// 提货到场:220
                pickArriver += 1;
                eventMap.put(eventModel.getOwbWayBillNo(), eventModel.getOperationType());
            }

            if ("270".equals(eventModel.getOperationType()) || "180".equals(eventModel.getOperationType())) {// 运抵:270，运抵确认：180
                signArriver += 1;
                eventMap.put(eventModel.getOwbWayBillNo(), eventModel.getOperationType());
            }

        }
        //logger.info("waybillCount:" + waybillCount + ",pickArriver:" + pickArriver + ",signArriver:" + signArriver);
        packageEventUpdate(billModels, eventMap);
    }

    /**
     * 更新运单的节点信息
     *
     * @param billModels
     * @param eventMap
     */
    private void packageEventUpdate(List<HandleWayBill> billModels, Map<String, String> eventMap) {
        List<HandleWayBill> udpateList = new ArrayList<>();
        for (HandleWayBill model : billModels) {
            if (eventMap.get(model.getOwbWayBillNo()) != null) {
                HandleWayBill saveModel = new HandleWayBill();
                saveModel.setOwbWayBillNo(model.getOwbWayBillNo());
                saveModel.setTrackEventNode("已" + eventMap.get(model.getOwbWayBillNo()));
                udpateList.add(saveModel);
            }
            if (model.getHasReceipt() != null && model.getHasReceipt().equals(0)) {
                HandleWayBill saveModel = new HandleWayBill();
                saveModel.setOwbWayBillNo(model.getOwbWayBillNo());
                saveModel.setTrackEventNode("已回单");
                udpateList.add(saveModel);
            }
        }
        if (EmptyUtils.isNotEmpty(udpateList)) {
            billManager.batchUpdateWaybillInfo(udpateList);
        }
    }

    /**
     * 转化set的数据成string
     *
     * @param exceptionSet
     * @return
     */
    private String transformateException(Set<String> exceptionSet) {
        StringBuilder exceptionMessage = new StringBuilder();
        if (EmptyUtils.isNotEmpty(exceptionSet)) {
            exceptionSet.stream().forEach(type -> exceptionMessage.append(type).append("、"));
        }
        return exceptionMessage.substring(0, exceptionMessage.length() - 1).toString();
    }

    /**
     * 封装预警类型与预警时长map集合
     *
     * @param ruleMap
     * @param alertRuleModels
     */
    private void generateRuleMap(Map<String, Integer> ruleMap, List<GyAlertRule> alertRuleModels) {
        if (EmptyUtils.isNotEmpty(alertRuleModels)) {
            for (GyAlertRule gyAlertRuleModel : alertRuleModels) {
                ruleMap.put(gyAlertRuleModel.getTransportModel(),
                        null == gyAlertRuleModel.getAdvanceAlertTime() ? 0 : gyAlertRuleModel.getAdvanceAlertTime());
            }
        } else {
            ruleMap.put(Globals.TRANSPORT_MODEL_CITY, 0);
            ruleMap.put(Globals.TRANSPORT_MODEL_LINE, 0);
        }
    }

    /**
     * 封装调度单与与温度计返回情况 map<调度单号,温度计返回情况>
     *
     * @param owbEcNoMap
     * @param owbEcNoMap
     */
    private void generateDispatchNoReturnTemperatureMap(List<HandleWayBill> billModels, Map<String, List<Integer>> owbEcNoMap) {
        if (EmptyUtils.isNotEmpty(billModels)) {
            for (HandleWayBill handleWayBill : billModels) {
                if (null != handleWayBill.getReturnTemperature()) {
                    if (owbEcNoMap.containsKey(handleWayBill.getOwbEcNo())) {
                        owbEcNoMap.get(handleWayBill.getOwbEcNo()).add(handleWayBill.getReturnTemperature());
                    } else {
                        List<Integer> gyInfoViews = new ArrayList<>();
                        gyInfoViews.add(handleWayBill.getReturnTemperature());
                        owbEcNoMap.put(handleWayBill.getOwbEcNo(), gyInfoViews);
                    }
                }
            }
        }
    }
}
