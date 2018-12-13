package com.jlt.datasync.dto;


import lombok.Data;

@Data
public class GyAlertMessageDto extends BaseDto {

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
     * 预警时间
     */
    private String alertTime;
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

}
