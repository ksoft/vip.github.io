package com.jlt.datasync.condition;

import lombok.Data;

import java.util.List;

@Data
public class GyReturnPrescriptionCondition extends PageCondition{
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

    private List<Long> ids;

    private String areaLike;
}
