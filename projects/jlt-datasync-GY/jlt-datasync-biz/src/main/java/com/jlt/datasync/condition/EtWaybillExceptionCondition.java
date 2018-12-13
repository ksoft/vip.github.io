package com.jlt.datasync.condition;

import lombok.Data;

import java.util.List;

@Data
public class EtWaybillExceptionCondition extends PageCondition {
    private static final long serialVersionUID = 1L;
    /**
     * 运单号
     */
    private String waybillNo;
    /**
     * 外部运单号
     */
    private String exWayBillNo;
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
     * 精确派车单号查询
     */
    private String owbEcNoAct;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 运单号pmcode集合
     */
    private List<String> waybillNos;
}
