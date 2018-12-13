package com.jlt.datasync.rowmapper;

import com.jlt.datasync.domain.JltJobDetail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public class JltJobDetailRowMapper implements RowMapper<JltJobDetail> {
    @Override
    public JltJobDetail mapRow(ResultSet resultSet, int i) throws SQLException {
        JltJobDetail jltJobDetail = new JltJobDetail();
        jltJobDetail.setId(resultSet.getLong("id"));
        jltJobDetail.setTenantId(resultSet.getLong("tenant_id"));
        jltJobDetail.setDepartmentId(resultSet.getLong("department_id"));
        jltJobDetail.setJobId(resultSet.getLong("job_id"));
        jltJobDetail.setJobType(resultSet.getString("job_type"));
        jltJobDetail.setSchemaName(resultSet.getString("schema_name"));
        jltJobDetail.setTableName(resultSet.getString("table_name"));
        jltJobDetail.setIsExcludeTenantId(resultSet.getString("is_exclude_tenant_id"));
        jltJobDetail.setTenantIdCondition(resultSet.getString("tenant_id_condition"));
        jltJobDetail.setIsExcludeTime(resultSet.getString("is_exclude_time"));
        jltJobDetail.setLastExecSuccessTime(resultSet.getTimestamp("last_exec_success_time"));
        jltJobDetail.setLastExecResult(resultSet.getString("last_exec_result"));
        jltJobDetail.setExtendWhereCondition(resultSet.getString("extend_where_condition"));
        jltJobDetail.setNoInsertColumns(resultSet.getString("no_insert_columns"));
        jltJobDetail.setNoUpdateColumns(resultSet.getString("no_update_columns"));
        jltJobDetail.setCustomerSql(resultSet.getString("customer_sql"));
        jltJobDetail.setOrderId(resultSet.getInt("order_id"));
        jltJobDetail.setRecVer(resultSet.getInt("rec_ver"));
        jltJobDetail.setRecStatus(resultSet.getInt("rec_status"));
        jltJobDetail.setCreateName(resultSet.getString("create_name"));
        jltJobDetail.setCreator(resultSet.getString("creator"));
        jltJobDetail.setCreateTime(resultSet.getDate("create_time"));
        jltJobDetail.setModifier(resultSet.getString("modifier"));
        jltJobDetail.setModifyName(resultSet.getString("modify_name"));
        jltJobDetail.setModifyTime(resultSet.getDate("modify_time"));
        return jltJobDetail;
    }
}
