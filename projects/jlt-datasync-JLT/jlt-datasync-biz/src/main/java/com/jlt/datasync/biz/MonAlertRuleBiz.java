package com.jlt.datasync.biz;

import com.jlt.datasync.condition.MonAlertRuleCondition;
import com.jlt.datasync.domain.MonAlertRule;

/**
 * @author Billy.Zhang
 * @date 2018/10/12
 */
public interface MonAlertRuleBiz {
    MonAlertRule findTemperatureAlertRule(MonAlertRuleCondition condition);

    void refreshMonAlertRuleCache(Long tenantId);
}
