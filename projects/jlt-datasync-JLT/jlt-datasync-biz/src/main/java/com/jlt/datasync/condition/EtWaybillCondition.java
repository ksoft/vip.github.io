package com.jlt.datasync.condition;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Data
@NoArgsConstructor
public class EtWaybillCondition extends BaseCondition{
    private String waybillNo;
    private String destinationAddress;
}
