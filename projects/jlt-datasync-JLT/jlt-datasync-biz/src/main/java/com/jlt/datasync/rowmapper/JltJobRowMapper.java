package com.jlt.datasync.rowmapper;

import com.jlt.datasync.domain.JltJob;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public class JltJobRowMapper implements RowMapper<JltJob> {

    @Override
    public JltJob mapRow(ResultSet resultSet, int i) throws SQLException {
        JltJob jltJob = new JltJob();
        jltJob.setId(resultSet.getLong("id"));
        jltJob.setTenantId(resultSet.getLong("tenant_id"));
        jltJob.setDepartmentId(resultSet.getLong("department_id"));
        jltJob.setJobName(resultSet.getString("job_name"));
        jltJob.setIsEnableSaas(resultSet.getString("is_enable_saas"));
        jltJob.setIsEnableUpm(resultSet.getString("is_enable_upm"));
        jltJob.setIsEnablePms(resultSet.getString("is_enable_pms"));
        jltJob.setLastExecSuccessTime(resultSet.getTimestamp("last_exec_success_time"));
        jltJob.setLastExecResult(resultSet.getString("last_exec_result"));
        jltJob.setRecVer(resultSet.getInt("rec_ver"));
        jltJob.setRecStatus(resultSet.getInt("rec_status"));
        jltJob.setCreateName(resultSet.getString("create_name"));
        jltJob.setCreator(resultSet.getString("creator"));
        jltJob.setCreateTime(resultSet.getDate("create_time"));
        jltJob.setModifier(resultSet.getString("modifier"));
        jltJob.setModifyName(resultSet.getString("modify_name"));
        jltJob.setModifyTime(resultSet.getDate("modify_time"));
        return jltJob;
    }
}
