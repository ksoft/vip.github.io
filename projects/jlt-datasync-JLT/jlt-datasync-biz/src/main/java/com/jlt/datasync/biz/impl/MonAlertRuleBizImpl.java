package com.jlt.datasync.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.jlt.common.constant.Globals;
import com.jlt.common.enums.YesNo;
import com.jlt.datasync.biz.MonAlertRuleBiz;
import com.jlt.datasync.condition.MonAlertRuleCondition;
import com.jlt.datasync.domain.MonAlertRule;
import com.jlt.datasync.mapper.saas.MonAlertRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Billy.Zhang
 * @date 2018/10/12
 */
@Service
public class MonAlertRuleBizImpl implements MonAlertRuleBiz {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MonAlertRuleMapper monAlertRuleMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public MonAlertRule findTemperatureAlertRule(MonAlertRuleCondition condition) {
        return monAlertRuleMapper.findTemperatureAlertRule(condition);
    }

    @Override
    public void refreshMonAlertRuleCache(Long tenantId) {
        logger.info("温度报警规则同步到Redis===start");
        try {
            String key = Globals.MON_ALERT_RULE_KEY_PREFIX + tenantId;
            MonAlertRuleCondition condition = new MonAlertRuleCondition();
            condition.setTenantId(tenantId);
            MonAlertRule monAlertRule = this.findTemperatureAlertRule(condition);
            if (YesNo.YES.getLowerKey().equals(monAlertRule.getIsEnable()) && YesNo.YES.getLowerKey()
                    .equals(monAlertRule.getIsTemperature())) {
                Map<String, String> map = new HashMap<>();
                map.put(Globals.TEMPERATURE_CYCLE_ALERT, monAlertRule.getTemperatureCycleAlert());
                map.put(Globals.TEMPERATURE_DATA_ALERT, monAlertRule.getTemperatureDataAlert());
                String dataStr = JSONObject.toJSONString(map);
                logger.info(dataStr);
                redisTemplate.opsForHash().put(key, Globals.TEMPERATURE_EXCEEDED_ALARM, dataStr);
            } else {
                redisTemplate.opsForHash().delete(key, Globals.TEMPERATURE_EXCEEDED_ALARM);
            }
        } catch (Exception e) {
            logger.error("温度报警规则同步到Redis 失败：" + e.getMessage());
        }
        logger.info("温度报警规则同步到Redis===end");
    }
}
