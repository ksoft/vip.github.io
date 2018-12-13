package com.jlt.datasync.domain;

import lombok.Data;

@Data
public class GyAlertMessage extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /**
     * 外部派车单号
     */
    private String exEcNo;
    /**
     * 派车单号
     */
    private String owbEcNo;
    /**
     * 预警内容
     */
    private String alertMessage;
    /**
     * 处理时间
     */
    private String handleTime;
    /**
     * 处理人
     */
    private String handler;
    /**
     * 处理意见（10已反馈 20已处理）
     */
    private Integer handleView;
    /**
     * 备注
     */
    private String remark;
    /**
     * 预警时间
     */
    private String alertTime;
}
