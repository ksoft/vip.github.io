package com.jlt.datasync.biz;

import com.jlt.datasync.condition.MonAlertRuleCondition;
import com.jlt.datasync.domain.MonAlertRule;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
public interface MonAlertRuleBiz {
    List<MonAlertRule> findByCondition(MonAlertRuleCondition monAlertCondition);
    MonAlertRule findOne(MonAlertRuleCondition condition);
}
