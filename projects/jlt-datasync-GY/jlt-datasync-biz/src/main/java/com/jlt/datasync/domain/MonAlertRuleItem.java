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
public class MonAlertRuleItem extends BaseDomain{
    private String pmCode;
    private String alertRulePmCode;
    private String ruleItemCode;
    private String ruleItemValue;
    private String itemDesc;
}
