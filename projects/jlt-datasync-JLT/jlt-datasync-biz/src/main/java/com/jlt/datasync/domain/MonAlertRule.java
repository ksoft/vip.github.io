package com.jlt.datasync.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/10/12
 */
@Data
@NoArgsConstructor
public class MonAlertRule extends BaseDomain{
    private String alertRuleType;
    private String alertRuleCode;
    private String isEnable;
    private String isTemperature;
    private String temperatureCycleAlert;
    private String temperatureDataAlert;
}
