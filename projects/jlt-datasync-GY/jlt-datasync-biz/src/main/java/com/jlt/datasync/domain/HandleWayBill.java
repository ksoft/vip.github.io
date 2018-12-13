package com.jlt.datasync.domain;

import java.util.Date;

import lombok.Data;

@Data
public class HandleWayBill extends BaseDomain {
    private String owbPmCode;
    private String owbWayBillNo;
    private String owbEcNo;
    private String eloNo;
    private String exWayBillNo;
    private String exEcNo;
    private Integer waybillStatus;
    private String owbIsSignException;
    private String owbShipperId;
    private String owbShipperName;
    private String owbOfficeId;
    private String owbOfficeName;
    private String owbCarNo;
    private String owbDriverName;
    private String owbDriverPhone;
    private String owbHasApp;
    private String owbHasGps;
    private String owbHasTemperature;
    private String owbHasDampness;
    private String waybillException;
    /**
     * 是否已回单
     */
    private Integer hasReceipt;
    private String planArrivalTime;
    private String signTime;
    /**
     * 运抵时间
     */
    private String shipTime;
    /**
     * 提货到场时间
     */
    private String pickupArriveTime;
    /**
     * 提货时间
     */
    private String pickupTime;
    /**
     * 要求回单时间
     */
    private String estimatedUploadTime;
    /**
     * 要求返单时间
     */
    private String expectedOriginalReturnTime;
    /**
     * 要求提货时间
     */
    private String requireDeliveryTime;
    /**
     * 要求送达时间
     */
    private String requiredArrivalTime;
    /**
     * 实际回单时间
     */
    private String actReceiptTime;
    /**
     * 实际返单时间
     */
    private String actReturnTime;

    private Integer trackOrderHandleFlag;
    private Integer returnTemperature;
    private Integer returnReceipt;
    private String operationType;
    private String operationTypeName;
    private String planMileage;
    private String owbdPmCode;
    private String destinationAreaName;
    private String destinationCityName;
    private String destinationProvinceName;
    private String originAreaName;
    private String originCityName;
    private String originProvinceName;
    private String gyTransportModel;
    private String trackEventNode;
    /**
     * 调度时间
     */
    private Date dispatchTime;

    /**
     * 是否已提货预警
     */
    private String isPickupEarlyWarning;
    /**
     * 是否已提货报警
     */
    private String isPickupAlarm;
    /**
     * 是否已送达预警
     */
    private String isArrivalEarlyWarning;
    /**
     * 是否已送达报警
     */
    private String isArrivalAlarm;
    /**
     * 是否已回单预警
     */
    private String isReceiptEarlyWarning;
    /**
     * 是否已回单报警
     */
    private String isReceiptAlarm;
    /**
     * 是否已返单预警
     */
    private String isReturnEarlyWarning;
    /**
     * 是否已返单报警
     */
    private String isReturnAlarm;
    /**
     * 车型
     */
    private String vehicleType;
    /**
     * 运单状态（国药）
     */
    private String gyWaybillStatus;
    private String operationTime;
    /**
     * 国药-要求送达时间
     */
    private String gyRequireArrivalTime;
    /**
     * 国药-要求退货时间
     */
    private String gyRequireReturnTime;
    /**
     * 国药-要求回单上传时间
     */
    private String gyRequireReceiptUploadTime;
    /**
     * 国药-要求回单返单时间
     */
    private String gyRequireReceiptReturnTime;
    /**
     * 国药-温度计要求返回时间
     */
    private String gyRequireThermometerReturnTime;

    /**
     * 收货方ID
     */
    private Long consigneeId;
    /**
     * 收货方名称
     */
    private String consigneeName;
    /**
     * 运输类型(10公路 20铁路 30海运 40空运)
     */
    private Integer transportType;
    /**
     * 运输模式(10零担 20整车)
     */
    private Integer transportMode;

    /**
     * 预计到达时间，通过当前GPS计算
     */
    private String predictArrivalTime;
}
