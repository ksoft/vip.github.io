package com.jlt.datasync.timer;

import com.jlt.common.util.CommonUtil;
import com.jlt.datasync.biz.EtWaybillExceptionBiz;
import com.jlt.datasync.biz.GyPrescriptionSettingBiz;
import com.jlt.datasync.condition.GyPrescriptionSettingCondition;
import com.jlt.datasync.domain.GyPrescriptionSetting;
import com.jlt.datasync.dto.EtWaybillExceptionDto;
import com.jlt.datasync.dto.GyPrescriptionMatchResultDto;
import com.sinoservices.common.date.DateJDK8SafeUtil;
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
 * 关于退货时效，更新异常预计退回时间，定时器处理
 *
 * @author Billy.Zhang
 */
@Component
@JobHander("gy:returnprescriptionhandle")
public class GyReturnRescriptionHandleJobTask extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EtWaybillExceptionBiz exceptionManager;
    @Autowired
    private GyPrescriptionSettingBiz gyPrescriptionSettingBiz;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("开始执行退货时效，定时任务");
        dataHandleTaskEntrance();
        logger.info("退货时效定时任务，执行成功");
        return ReturnT.SUCCESS;
    }

    /**
     * 处理数据的入口
     */
    private void dataHandleTaskEntrance() {
        // 获取承运商下面的异常处理
        List<EtWaybillExceptionDto> billExceptionViews = exceptionManager.getExceptions();
        logger.info("获取异常数据，获取记录数：" + (EmptyUtils.isEmpty(billExceptionViews) ? 0 : billExceptionViews.size()));
        if (EmptyUtils.isEmpty(billExceptionViews)) {
            logger.info("获取异常数量为0，任务终止");
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
        List<EtWaybillExceptionDto> updateList = new ArrayList<>();
        for (EtWaybillExceptionDto exception : billExceptionViews) {
            GyPrescriptionMatchResultDto resultDto = null;
            for (GyPrescriptionSetting setting : gyPrescriptionSettingList) {
                GyPrescriptionMatchResultDto curDto = new GyPrescriptionMatchResultDto();
                BeanUtils.copyProperties(setting, curDto);
                if (exception.getShipperName().equals(setting.getShipperName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsShipperMatch(true);
                }
                if (exception.getCompanyName().equals(setting.getCompanyName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsCompanyMatch(true);
                }
                if (exception.getConsigneeName().equals(setting.getConsigneeName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsConsigneeMatch(true);
                }
                if (exception.getOriginProvinceName().equals(setting.getOriginProvinceName()) && exception.getOriginCityName()
                        .equals(setting.getOriginCityName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsOriginProvinceCityMatch(true);
                }
                if (exception.getDestinationProvinceName().equals(setting.getDestinationProvinceName()) && exception
                        .getDestinationCityName().equals(setting.getDestinationCityName())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsDestinationProvinceCityMatch(true);
                }
                if (exception.getTransportType() != null && exception.getTransportType().equals(setting.getTransportType())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsTransportTypeMatch(true);
                }
                if (exception.getTransportMode() != null && exception.getTransportMode().equals(setting.getTransportMode())) {
                    curDto.setCount(curDto.getCount() + 1);
                    curDto.setIsTransportModeMatch(true);
                }
                if (resultDto == null) {
                    //如果当前有两个以上字段匹配，则将初始dto赋值给resultDto
                    if (curDto.getCount() >= 2) {
                        resultDto = curDto;
                    }
                } else {
                    //若运单匹配上两条及以上的规则，则按照匹配字段多的优先
                    if (curDto.getCount() > resultDto.getCount()) {
                        resultDto = curDto;
                        //若运单匹配上两条及以上的规则，并且匹配上的字段数一致，则优先顺序：1货主、2承运商、3客户名称、4出发城市、5目的城市、6运输方式、7配载方式
                    } else if (curDto.getCount().equals(resultDto.getCount())) {
                        if (curDto.getIsShipperMatch() && !resultDto.getIsShipperMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsCompanyMatch() && !resultDto.getIsCompanyMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsConsigneeMatch() && !resultDto.getIsConsigneeMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsOriginProvinceCityMatch() && !resultDto.getIsOriginProvinceCityMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsDestinationProvinceCityMatch() && !resultDto.getIsDestinationProvinceCityMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsTransportTypeMatch() && !resultDto.getIsTransportTypeMatch()) {
                            resultDto = curDto;
                        } else if (curDto.getIsTransportModeMatch() && !resultDto.getIsTransportModeMatch()) {
                            resultDto = curDto;
                        }
                    }
                }
                /*logger.info(JSON.toJSONString(exception));
                logger.info(resultDto == null ? "null" : JSON.toJSONString(resultDto));*/
            }
            //强制全匹配，如果不强制全匹配，去除resultDto.getCount()==7判断就可以
            if (resultDto != null && resultDto.getCount() == 7) {
                if (!StringUtils.isEmpty(exception.getSignTime())) {
                    //要求退货时间
                    Long minusReturn = BigDecimal.valueOf(resultDto.getReturnPrescription() * 60).longValue();
                    LocalDateTime dateTimeReturn =
                            LocalDateTime.parse(exception.getSignTime(), DateTimeFormatter.ofPattern(DateJDK8SafeUtil.YYYY_MM_DD_HH_MI_SS))
                                    .plus(minusReturn, ChronoUnit.MINUTES);
                    exception.setGyExpectedReturnTime(CommonUtil.localDateTime2Date(dateTimeReturn));
                }
                updateList.add(exception);
            }
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            logger.info("批量更新异常，预计退回时间");
            exceptionManager.batchUpdateGyExceptionReturnTime(billExceptionViews);
        }
    }
}
