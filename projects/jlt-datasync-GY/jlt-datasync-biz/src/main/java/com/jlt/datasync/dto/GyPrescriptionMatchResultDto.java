package com.jlt.datasync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/11/2
 */
@Data
@NoArgsConstructor
public class GyPrescriptionMatchResultDto {
    /**
     * 是否有匹配上除了运输方式与配载方式之外的选项，只要匹配一个就是true
     */
    private Boolean isMatchForBasic = false;
    private Long count=0L;
    private Boolean isShipperMatch=false;
    private Boolean isCompanyMatch=false;
    private Boolean isConsigneeMatch=false;
    private Boolean isOriginProvinceCityMatch=false;
    private Boolean isDestinationProvinceCityMatch=false;
    private Boolean isTransportTypeMatch=false;
    private Boolean isTransportModeMatch=false;
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
