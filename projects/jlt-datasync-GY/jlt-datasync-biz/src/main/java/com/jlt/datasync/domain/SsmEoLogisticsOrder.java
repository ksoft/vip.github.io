package com.jlt.datasync.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/10/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsmEoLogisticsOrder extends BaseDomain{
    private String eloNo;
    private String custEloNo;
    private String eloStatus;
    private Integer isHandle;
}
