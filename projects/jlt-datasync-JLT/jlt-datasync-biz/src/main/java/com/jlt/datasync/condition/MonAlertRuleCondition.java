package com.jlt.datasync.condition;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Data
@NoArgsConstructor
public class MonAlertRuleCondition extends BaseCondition{
    private String alertRuleType;
    private String alertRuleCode;
    private String isEnable;
    private String isTemperature;
    private String temperatureCycleAlert;
    private String temperatureDataAlert;
}
