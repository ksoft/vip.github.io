package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.MonAlertRuleBiz;
import com.jlt.datasync.condition.MonAlertRuleCondition;
import com.jlt.datasync.domain.MonAlertRule;
import com.jlt.datasync.mapper.saas.MonAlertRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
@Service
public class MonAlertRuleBizImpl implements MonAlertRuleBiz{
    @Autowired
    private MonAlertRuleMapper monAlertRuleMapper;

    @Override
    public List<MonAlertRule> findByCondition(MonAlertRuleCondition monAlertCondition) {
        return monAlertRuleMapper.findByCondition(monAlertCondition);
    }

    @Override
    public MonAlertRule findOne(MonAlertRuleCondition condition) {
        return monAlertRuleMapper.findOne(condition);
    }


}
