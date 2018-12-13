package com.jlt.datasync.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Billy.Zhang
 * @date 2018/11/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GyAlertNoticeSetting extends BaseDomain {
    /**
     * 时效from
     */
    private BigDecimal prescriptionFrom;
    /**
     * 时效to
     */
    private BigDecimal prescriptionTo;
    /**
     * 接收人姓名
     */
    private String receiverName;
    /**
     * 平台帐号
     */
    private String platformAccount;
}
