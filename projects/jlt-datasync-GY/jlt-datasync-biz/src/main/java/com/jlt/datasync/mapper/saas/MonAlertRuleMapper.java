package com.jlt.datasync.mapper.saas;

import com.jlt.datasync.condition.MonAlertRuleCondition;
import com.jlt.datasync.domain.MonAlertRule;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
public interface MonAlertRuleMapper {
    List<MonAlertRule> findByCondition(MonAlertRuleCondition monAlertCondition);

    MonAlertRule findOne(MonAlertRuleCondition condition);
}
