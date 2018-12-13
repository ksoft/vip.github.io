package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.MonAlertRuleItemBiz;
import com.jlt.datasync.condition.MonAlertRuleItemCondition;
import com.jlt.datasync.domain.MonAlertRuleItem;
import com.jlt.datasync.mapper.saas.MonAlertRuleItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/18
 */
@Service
public class MonAlertRuleItemBizImpl implements MonAlertRuleItemBiz{
    @Autowired
    private MonAlertRuleItemMapper monAlertRuleItemMapper;

    @Override
    public List<MonAlertRuleItem> findByCondition(MonAlertRuleItemCondition monAlertRuleItemCondition) {
        return monAlertRuleItemMapper.findByCondition(monAlertRuleItemCondition);
    }
}
