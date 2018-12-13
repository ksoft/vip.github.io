package com.jlt.datasync.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/9/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtWaybillException extends BaseDomain{
    /**
     * 异常编号
     */
    private String exceptionNo;
    /**
     * 系统运单号
     */
    private String waybillNo;
    /**
     * 外部运单号
     */
    private String exWaybillNo;
    /**
     * 运单状态
     */
    private Integer waybillStatus;
    /**
     * 异常类型
     */
    private String exceptionType;
    /**
     * 异常类型名称
     */
    private String exceptionTypeName;
    /**
     * 异常子类型
     */
    private String exceptionSubType;
    /**
     * 异常子类型名称
     */
    private String exceptionSubTypeName;
    /**
     * 异常描述
     */
    private String exceptionContent;
    /**
     * 异常发生时间
     */
    private String exceptionTime;
    /**
     * 异常发生地点
     */
    private String exceptionAddress;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 异常件数
     */
    private Integer exceptionNumber;
    /**
     * 异常数量
     */
    private String exceptionQuantity;
    /**
     * 异常原因
     */
    private String exceptionReason;
    /**
     * 异常备注
     */
    private String exceptionRemark;
    /**
     * 异常登记人
     */
    private String registerPerson;
    /**
     * 附件数
     */
    private Integer exceptionFileNum;
    /**
     * 登记时间
     */
    private String registerTime;
    /**
     * 来源渠道(10:APP 20:WEB 30:手动新增)
     */
    private Integer sourceChannel;
    /**
     * 责任方
     */
    private String responsibleParty;
    /**
     * 责任方联系方式
     */
    private String contactTel;
    /**
     * 预计退回时间
     */
    private String expectReturnTime;
    
    private Integer handleFlag;

    private Integer gyHandleFlag;

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
}
