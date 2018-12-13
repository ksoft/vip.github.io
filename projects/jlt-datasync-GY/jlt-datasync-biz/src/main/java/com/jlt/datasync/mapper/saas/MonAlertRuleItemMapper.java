package com.jlt.datasync.mapper.saas;

import com.jlt.datasync.condition.MonAlertRuleItemCondition;
import com.jlt.datasync.domain.MonAlertRuleItem;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
public interface MonAlertRuleItemMapper {
    List<MonAlertRuleItem> findByCondition(MonAlertRuleItemCondition monAlertRuleItemCondition);
}
