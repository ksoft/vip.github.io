package com.jlt.datasync.condition;

import com.jlt.common.enums.YesNo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Data
@NoArgsConstructor
public class JltJobCondition extends BaseCondition{
    private String jobName;
    private String isEnableSaas;
    private String isEnableUpm;
    private String isEnablePms;
    private Date lastExecSuccessTime;
    private String lastExecResult;
}
