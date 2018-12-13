package com.jlt.datasync.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JltJobDetail extends BaseDomain {
    private Long jobId;
    private String jobType;
    private String schemaName;
    private String tableName;
    private String isExcludeTime;
    private String isExcludeTenantId;
    /**
     * 数据来源
     */
    private String dataReadSource;
    /**
     * redis的Key
     */
    private String redisKey;
    /**
     * tenantId过虑条件
     */
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
    /**
     * 个性化SQL，如果配置了此字段，则替代Select * from schemaName.tableName
     */
    private String customerSql;
    private String msg;
}
