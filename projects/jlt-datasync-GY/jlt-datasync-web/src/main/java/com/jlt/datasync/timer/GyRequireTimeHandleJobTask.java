package com.jlt.datasync.timer;

import com.jlt.common.util.CommonUtil;
import com.jlt.datasync.biz.EtWaybillExceptionBiz;
import com.jlt.datasync.biz.GyPrescriptionSettingBiz;
import com.jlt.datasync.biz.HadleWayBillBiz;
import com.jlt.datasync.condition.EtWaybillExceptionCondition;
import com.jlt.datasync.condition.GyPrescriptionSettingCondition;
import com.jlt.datasync.domain.GyPrescriptionSetting;
import com.jlt.datasync.domain.HandleWayBill;
import com.jlt.datasync.dto.EtWaybillExceptionDto;
import com.jlt.datasync.dto.GyPrescriptionMatchResultDto;
import com.sinoservices.common.date.DateJDK8SafeUtil;
import com.sinoservices.minima.common.util.EmptyUtil;
import com.sinoservices.xframework.core.utils.EmptyUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 要求时间计算任务
 *
 * @author Billy.Zhang
 * @date 2018/11/2
 */
@Component
@JobHander("gy:requireTime")
public class GyRequireTimeHandleJobTask extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GyPrescriptionSettingBiz gyPrescriptionSettingBiz;
    @Autowired
    private HadleWayBillBiz billManager;
    @Autowired
    private EtWaybillExceptionBiz etWaybillExceptionBiz;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("开始执行计算要求时间，定时任务");
        dataHandleTaskEntrance();
        logger.info("计算要求时间定时任务，执行成功");
        return ReturnT.SUCCESS;
    }

    /**
     * 处理数据的入口
     */
    private void dataHandleTaskEntrance() {
        List<HandleWayBill> billModels = billManager.getUnSetRequireTimeWaybill();
        logger.info("获取运单，记录数：" + (EmptyUtils.isEmpty(billModels) ? 0 : billModels.size()));
        if (EmptyUtils.isEmpty(billModels)) {
            logger.info("获取运单数量为0，任务终止");
            return;
        }
        // 查询时效设置
        GyPrescriptionSettingCondition condition = new GyPrescriptionSettingCondition();
        condition.setRecStatus(0);
        List<GyPrescriptionSetting> gyPrescriptionSettingList = gyPrescriptionSettingBiz.findList(condition);
        if (CollectionUtils.isEmpty(gyPrescriptionSettingList)) {
            logger.info("获取时效设置数量为0，任务终止");
            return;
        }
        List<HandleWayBill> updateList = new ArrayList<>();
        for (HandleWayBill wayBill : billModels) {
            GyPrescriptionMatchResultDto resultDto = null;
            for (GyPrescriptionSetting setting : gyPrescriptionSettingList) {
                GyPrescriptionMatchResultDto curDto = new GyPrescriptionMatchResultDto();
                BeanUtils.copyProperties(setting, curDto);
                if (!StringUtils.isEmpty(setting.getShipperName()) && wayBill.getOwbShipperName().equals(setting.getShipperName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsShipperMatch(true);
                    curDto.setIsMatchForBasic(true);
                }
                if (wayBill.getOwbOfficeName().equals(setting.getCompanyName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsCompanyMatch(true);
                    curDto.setIsMatchForBasic(true);
                }
                if (!StringUtils.isEmpty(setting.getConsigneeName()) && wayBill.getConsigneeName().equals(setting.getConsigneeName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsConsigneeMatch(true);
                    curDto.setIsMatchForBasic(true);
                }
                if (wayBill.getOriginProvinceName().equals(setting.getOriginProvinceName()) && wayBill.getOriginCityName()
                        .equals(setting.getOriginCityName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsOriginProvinceCityMatch(true);
                    curDto.setIsMatchForBasic(true);
                }
                if (wayBill.getDestinationProvinceName().equals(setting.getDestinationProvinceName()) && wayBill.getDestinationCityName()
                        .equals(setting.getDestinationCityName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsDestinationProvinceCityMatch(true);
                    curDto.setIsMatchForBasic(true);
                }
                if (wayBill.getTransportType() != null && wayBill.getTransportType().equals(setting.getTransportType())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsTransportTypeMatch(true);
                }
                if (wayBill.getTransportMode() != null && wayBill.getTransportMode().equals(setting.getTransportMode())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsTransportModeMatch(true);
                }
                if (resultDto == null) {
                    //如果除运输方式和配置方式外，当前有两个以上字段匹配，则将初始dto赋值给resultDto
                    if (curDto.getIsMatchForBasic() && curDto.getCount() >= 2) {
                        resultDto = curDto;
                    }
                } else {
                    //若运单匹配上两条及以上的规则，则按照匹配字段多的优先
                    if (curDto.getCount() > resultDto.getCount()) {
                        resultDto = curDto;
                        //若运单匹配上两条及以上的规则，并且匹配上的字段数一致，则优先顺序：货主、承运商、运输方式、配载方式、发货地、收货地信息、客户
                    } else if (curDto.getCount().equals(resultDto.getCount())) {
                        if (curDto.getIsShipperMatch() && !resultDto.getIsShipperMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsCompanyMatch() && !resultDto.getIsCompanyMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsTransportTypeMatch() && !resultDto.getIsTransportTypeMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsTransportModeMatch() && !resultDto.getIsTransportModeMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsOriginProvinceCityMatch() && !resultDto.getIsOriginProvinceCityMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsDestinationProvinceCityMatch() && !resultDto.getIsDestinationProvinceCityMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsConsigneeMatch() && !resultDto.getIsConsigneeMatch()) {
                            resultDto = curDto;
                        }
                    }
                }
                /*logger.info(JSON.toJSONString(wayBill));
                logger.info(resultDto == null ? "null" : JSON.toJSONString(resultDto));*/
            }

            if (resultDto != null) {
                //要求送达时间
                if (resultDto.getArrivalPrescription()!=null && StringUtils.isEmpty(wayBill.getGyRequireArrivalTime()) && !StringUtils.isEmpty(wayBill.getPickupTime())) {
                    Long minus = BigDecimal.valueOf(resultDto.getArrivalPrescription() * 60).longValue();
                    LocalDateTime dateTime =
                            LocalDateTime.parse(wayBill.getPickupTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))
                                    .plus(minus, ChronoUnit.MINUTES);
                    wayBill.setGyRequireArrivalTime(CommonUtil.localDateTime2String(dateTime));
                }

                if (!StringUtils.isEmpty(wayBill.getSignTime())) {
                    EtWaybillExceptionCondition etWaybillExceptionCondition = new EtWaybillExceptionCondition();
                    etWaybillExceptionCondition.setWaybillNo(wayBill.getOwbWayBillNo());
                    List<EtWaybillExceptionDto> etWaybillExceptionDtoList =
                            etWaybillExceptionBiz.findRejectException(etWaybillExceptionCondition);
                    //只有签收异常-拒收的运单，才会更新退货时间
                    if (EmptyUtil.isNotEmpty(etWaybillExceptionDtoList)) {
                        //要求退货时间
                        if (resultDto.getReturnPrescription() != null && StringUtils.isEmpty(wayBill.getGyRequireReturnTime())) {
                            Long minusReturn = BigDecimal.valueOf(resultDto.getReturnPrescription() * 60).longValue();
                            LocalDateTime dateTimeReturn = LocalDateTime
                                    .parse(wayBill.getSignTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))
                                    .plus(minusReturn, ChronoUnit.MINUTES);
                            wayBill.setGyRequireReturnTime(CommonUtil.localDateTime2String(dateTimeReturn));
                        }
                    }
                    //要求回单上传时间
                    if (resultDto.getReceiptUploadPrescription()!=null && StringUtils.isEmpty(wayBill.getGyRequireReceiptUploadTime())) {
                        Long minusUpload = BigDecimal.valueOf(resultDto.getReceiptUploadPrescription() * 60).longValue();
                        LocalDateTime dateTimeUpload =
                                LocalDateTime.parse(wayBill.getSignTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))
                                        .plus(minusUpload, ChronoUnit.MINUTES);
                        wayBill.setGyRequireReceiptUploadTime(CommonUtil.localDateTime2String(dateTimeUpload));
                    }
                    //要求回单返单时间
                    if (resultDto.getReceiptReturnPrescription()!=null && StringUtils.isEmpty(wayBill.getGyRequireReceiptReturnTime())) {
                        Long minusReceiptReturn = BigDecimal.valueOf(resultDto.getReceiptReturnPrescription() * 60).longValue();
                        LocalDateTime dateTimeReceiptReturn =
                                LocalDateTime.parse(wayBill.getSignTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))
                                        .plus(minusReceiptReturn, ChronoUnit.MINUTES);
                        wayBill.setGyRequireReceiptReturnTime(CommonUtil.localDateTime2String(dateTimeReceiptReturn));
                    }
                    //要求温度计返回时间
                    if (resultDto.getThermometerReturnPrescription()!=null && StringUtils.isEmpty(wayBill.getGyRequireThermometerReturnTime())) {
                        Long minusThermometerReturn = BigDecimal.valueOf(resultDto.getThermometerReturnPrescription() * 60).longValue();
                        LocalDateTime dateTimeThermometerReturn =
                                LocalDateTime.parse(wayBill.getSignTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))
                                        .plus(minusThermometerReturn, ChronoUnit.MINUTES);
                        wayBill.setGyRequireThermometerReturnTime(CommonUtil.localDateTime2String(dateTimeThermometerReturn));
                    }
                }
                updateList.add(wayBill);
            }
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            billManager.batchUpdateWaybillInfoExt(updateList);
            logger.info("WaybillInfoExt更新成功数量：" + updateList.size());
        }
    }
}
