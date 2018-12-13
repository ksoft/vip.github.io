package com.jlt.datasync.domain;

import lombok.Data;

import java.util.Date;

@Data
public class GyjlDispatchOrder extends BaseDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 派车单号
     */
    private String owbEcNo;
    /**
     * 物流单号
     */
    private String eloNo;
    /**
     * 外部派车单号
     */
    private String exEcNo;
    /**
     * 车牌号
     */
    private String owbCarNo;
    /**
     * 货主ID
     */
    private String owbShipperId;
    /**
     * 货主名称
     */
    private String owbShipperName;
    /**
     * 承运商名称
     */
    private String owbOfficeName;
    /**
     * 承运商ID
     */
    private String owbOfficeId;
    /**
     * 调度时间
     */
    private Date dispatchTime;
    /**
     * 异常状态(10 正常 20异常)
     */
    private Integer exceptionStatus;
    /**
     * 异常状态(10 未处理 20已处理)
     */
    private Integer handleStatus;
    /**
     * 异常描述
     */
    private String exceptionMessage;
    /**
     * 是否GPS跟踪( 1有1无)
     */
    private Integer hasGps;
    /**
     * 是否温度跟踪( 1有1无)
     */
    private Integer hasTemperture;
    /**
     * 时效跟踪(10 超时预警 20已超时 30正常 )
     */
    private Integer trackStatus;
    /**
     * 执行状态(10 提货到场 20在途 30 运抵 40签收 50回单)
     */
    private Integer dispatchStatus;
    /**
     * 计划里程
     */
    private String planMileage;
    /**
     * 实际里程
     */
    private String actMileage;
    /**
     * 温度计返回情况(10 未返回 20返回部分 30 已全返)
     */
    private Integer returnTemperature;
    /**
     * 返单情况(10 未返回 20返回部分 30已全返 )
     */
    private Integer returnReceipt;
    /**
     * 进度
     */
    private Integer dispatchProgress;
    /**
     * 司机号码
     */
    private String owbDriverPhone;
    /**
     * 司机名称
     */
    private String owbDriverName;
    /**
     * 车型
     */
    private String vehicleType;
}
