package com.jlt.datasync.condition;

import lombok.Data;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
@Data
public class MonAlertRuleCondition extends PageCondition{
    private String isEnable;

    private String alertRuleCode;
}
