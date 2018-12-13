package com.jlt.datasync.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Billy.Zhang
 * @date 2018/10/13
 */
@Data
@NoArgsConstructor
public class EtWaybill extends BaseDomain {
    private String waybillNo;
    private String vehicleNumber;
    private String destinationAddress;
}
