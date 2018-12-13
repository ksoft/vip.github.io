package com.jlt.datasync.domain;

import lombok.Data;

/**
 * @author Billy.Zhang
 * @date 2018/11/2
 */
@Data
public class GyPrescriptionSetting extends BaseDomain {
    /**
     * 发货方ID（货主）
     */
    private Long shipperId;
    /**
     * 发货方名称（货主）
     */
    private String shipperName;
    /**
     * 供应商租户ID（承运商）
     */
    private Long companyId;
    /**
     * 供应商编码（承运商）
     */
    private String companyCode;
    /**
     * 供应商名称（承运商）
     */
    private String companyName;
    /**
     * 收货方ID（客户）
     */
    private Long consigneeId;
    /**
     * 收货方名称（客户）
     */
    private String consigneeName;
    /**
     * 起运地省代码（出发城市）
     */
    private String originProvinceId;
    /**
     * 起运地省名称（出发城市）
     */
    private String originProvinceName;
    /**
     * 起运地城市代码（出发城市）
     */
    private String originCityId;
    /**
     * 起运地城市名称（出发城市）
     */
    private String originCityName;
    /**
     * 目的地省代码(目的地市）
     */
    private String destinationProvinceId;
    /**
     * 目的地省名称(目的地市）
     */
    private String destinationProvinceName;
    /**
     * 目的地城市代码(目的地市）
     */
    private String destinationCityId;
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
     * 送达时效
     */
    private Double arrivalPrescription;
    /**
     * 回单上传时效
     */
    private Double receiptUploadPrescription;
    /**
     * 回单返单时效
     */
    private Double receiptReturnPrescription;
    /**
     * 温度计返回时效
     */
    private Double thermometerReturnPrescription;
    /**
     * 退货时效
     */
    private Double returnPrescription;
}
