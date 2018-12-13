package com.jlt.datasync.mapper.saas;

import com.jlt.datasync.condition.MonAlertRuleCondition;
import com.jlt.datasync.domain.MonAlertRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Mapper
public interface MonAlertRuleMapper {
    MonAlertRule findTemperatureAlertRule(MonAlertRuleCondition condition);
}
