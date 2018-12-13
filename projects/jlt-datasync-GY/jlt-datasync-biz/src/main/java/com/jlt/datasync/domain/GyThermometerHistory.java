package com.jlt.datasync.domain;

import lombok.Data;

/**
 * @author Billy.Zhang
 * @date 2018/11/3
 */
@Data
public class GyThermometerHistory extends BaseDomain {
    /**
     * 运单号
     */
    private String waybillNo;
    /**
     * 温度计编号
     */
    private String thermometerNo;
    /**
     * 主驾
     */
    private String mainDriverName;
    /**
     * 车牌号
     */
    private String vehicleNumber;
    /**
     * 温度计返回时间
     */
    private String temperatureReceiptTime;
    /**
     * 是否已温度计返回预警，是：Y'
     */
    private String isThermometerEarlyWarning;
    /**
     * 是否已温度计返回报警，是：Y
     */
    private String isThermometerAlarm;

}
