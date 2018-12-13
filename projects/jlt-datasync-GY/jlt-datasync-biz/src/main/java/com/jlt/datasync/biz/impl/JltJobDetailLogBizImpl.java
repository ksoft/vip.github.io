package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.JltJobDetailLogBiz;
import com.jlt.datasync.domain.JltJobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Service
public class JltJobDetailLogBizImpl implements JltJobDetailLogBiz {
    @Autowired
    @Qualifier("pmsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insertLog(JltJobDetail jltJobDetail, String msg, Date createTime) {
        String sql = "insert into jlt_job_detail_log(tenant_id,job_detail_id,"
                + "schema_name,table_name,msg,rec_ver,rec_status,creator,create_name,create_time)" + "values(?,?,?,?,?,?,?,?,?,?)";
        Object args[] = {jltJobDetail.getTenantId(), jltJobDetail.getId(), jltJobDetail.getSchemaName(),jltJobDetail.getTableName(), msg, 0, 0, "sys", "sys",createTime};
        int cnt = jdbcTemplate.update(sql, args);
        return cnt;
    }
}
