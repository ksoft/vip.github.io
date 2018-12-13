package com.jlt.datasync.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonAlertOrder extends BaseDomain{
    private String pmCode;
    private String alertNo;
    private String alertRuleCode;
    private String alertType;
    private String itemDesc;
}
