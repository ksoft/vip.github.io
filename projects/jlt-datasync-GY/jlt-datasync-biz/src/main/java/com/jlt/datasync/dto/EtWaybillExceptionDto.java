package com.jlt.datasync.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EtWaybillExceptionDto extends BaseDto {
    private static final long serialVersionUID = 1L;
    /**
     * 异常编号
     */
    private String exceptionNo;
    /**
     * 运单号
     */
    private String waybillNo;
    /**
     * 外部运单号
     */
    private String exWaybillNo;
    /**
     * 异常时间
     */
    private String exceptionTime;
    /**
     * 异常大类
     */
    private String exceptionBroadHeading;
    /**
     * 异常类型
     */
    private String exceptionType;
    /**
     * 异常状态
     */
    private String exceptionStatus;
    /**
     * 异常发生地点
     */
    private String exceptionPlace;
    /**
     * 异常登记人
     */
    private String exceptionRegistrant;
    /**
     * 异常登记时间
     */
    private String exceptionRegisterTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 确认状态(10.未确认 20.已确认 30.已驳回)
     */
    private String comfirmStatus;
    /**
     * 确认人
     */
    private String confirmor;
    /**
     * 确认时间
     */
    private String confirmTime;
    /**
     * 处理标记（0否1是）
     */
    private Integer handleFlag;
    /**
     * 操作地址
     */
    private String operatorPlace;
    /**
     * 处理标记（0否1是）
     */
    private Integer gyHandleFlag;
    /**
     * 回退时间
     */
    private Date gyExpectedReturnTime;
    /**
     * 签收时间
     */
    private String signTime;
    /**
     * 提货时间
     */
    private String pickupTime;
    /**
     * 发货方
     */
    private String shipperName;
    /**
     * 承运商
     */
    private String companyName;
    /**
     * 收货方名称
     */
    private String consigneeName;
    /**
     * 起运地省名称（出发城市）
     */
    private String originProvinceName;
    /**
     * 起运地城市名称（出发城市）
     */
    private String originCityName;
    /**
     * 目的地省名称(目的地市）
     */
    private String destinationProvinceName;
    /**
     * 目的地城市名称(目的地市）
     */
    private String destinationCityName;
    /**
     * 运输方式(10公路 20铁路 30海运 40空运)
     */
    private Integer transportType;
    /**
     * 配载方式(10零担 20整车)
     */
    private Integer transportMode;
    /**
     * 是否已退货预警
     */
    private String isReturnGoodsEarlyWarning;
    /**
     * 是否已退货报警
     */
    private String isReturnGoodsAlarm;
    /**
     * 审核时间
     */
    private String auditTime;
    /**
     * 是否异常，0:是；1:否
     */
    private Integer isException;
    /**
     * 上一次预报警通知的id,ref gy_alert_notice_setting.id
     */
    private Long lastNoticeSettingId;
    /**
     * 异常内容
     */
    private String exceptionContent;
}
