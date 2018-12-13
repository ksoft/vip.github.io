package com.jlt.datasync.rowmapper;

import com.jlt.datasync.domain.JltJob;
import com.jlt.datasync.domain.MonAlertRule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public class MonAlertRuleRowMapper implements RowMapper<MonAlertRule> {

    @Override
    public MonAlertRule mapRow(ResultSet resultSet, int i) throws SQLException {
        MonAlertRule monAlertRule = new MonAlertRule();
        monAlertRule.setAlertRuleType(resultSet.getString("alert_rule_type"));
        monAlertRule.setAlertRuleCode(resultSet.getString("alert_rule_code"));
        monAlertRule.setIsEnable(resultSet.getString("is_enable"));
        monAlertRule.setIsTemperature(resultSet.getString("is_temperature"));
        monAlertRule.setTemperatureCycleAlert(resultSet.getString("temperature_cycle_alert"));
        monAlertRule.setTemperatureDataAlert(resultSet.getString("temperature_data_alert"));
        return monAlertRule;
    }
}
