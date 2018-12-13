package com.jlt.datasync.domain;

import lombok.Data;

@Data
public class GyReturnPrescription extends BaseDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 省ID
     */
    private String provinceId;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 市ID
     */
    private String cityId;
    /**
     * 退货时效
     */
    private Integer returnPrescription;
}
