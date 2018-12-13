package com.jlt.datasync.condition;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Data
@NoArgsConstructor
public class JltJobDetailCondition extends BaseCondition{
    private Long jobId;
    private String jobType;
    private String schemaName;
    private String tableName;
    private String isExcludeTime;
    private String isExcludeTenantId;
    private String tenantIdCondition;
    /**
     * 不插入也不更新的字段
     */
    private String noInsertColumns;
    /**
     * 只不更新的字段
     */
    private String noUpdateColumns;
    private Date lastExecSuccessTime;
    private String lastExecResult;
    private String extendWhereCondition;
    private Integer orderId;
    private String customerSql;
}
