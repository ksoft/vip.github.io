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
@NoArgsConstructor
@AllArgsConstructor
public class JltJob extends BaseDomain {
    private String jobName;
    private String isEnableSaas;
    private String isEnableUpm;
    private String isEnablePms;
    private Date lastExecSuccessTime;
    private String lastExecResult;
    private String msg;
}
