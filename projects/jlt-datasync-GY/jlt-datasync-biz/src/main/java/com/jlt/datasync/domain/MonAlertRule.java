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
public class MonAlertRule extends BaseDomain{
    private String pmCode;
    private String alertRuleType;
    private String alertRuleCode;
    private String alertType;
    private String ruleType;
    private String isEnable;
    private String isNotify;
    private String isSms;
    private String isEmail;
    private String isMessage;
    private String isTemperature;
    private String temperatureCycleAlert;
    private String temperatureDataAlert;
    private String temperatureIntervalAlert;
}
