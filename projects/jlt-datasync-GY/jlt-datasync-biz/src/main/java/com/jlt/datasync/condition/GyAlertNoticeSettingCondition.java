package com.jlt.datasync.condition;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Billy.Zhang
 * @date 2018/11/20
 */
@Data
@NoArgsConstructor
public class GyAlertNoticeSettingCondition extends BaseCondition{
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
