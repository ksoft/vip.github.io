package com.jlt.datasync.dto;

import lombok.Data;

@Data
public class GyAlertRuleDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    /**
     * 货主ID
     */
    private String owbShipperId;
    /**
     * 货主名称
     */
    private String owbShipperName;
    /**
     * 提前预警时长
     */
    private Integer advanceAlertTime;
    /**
     * 运输类型(10干线 20市配)
     */
    private String transportModel;
}
