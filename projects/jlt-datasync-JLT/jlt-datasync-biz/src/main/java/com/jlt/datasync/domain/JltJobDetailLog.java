package com.jlt.datasync.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Billy.Zhang
 * @date 2018/9/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JltJobDetailLog extends BaseDomain {
    private Long jobDetailId;
    private String schemaName;
    private String tableName;
    private String msg;
}
