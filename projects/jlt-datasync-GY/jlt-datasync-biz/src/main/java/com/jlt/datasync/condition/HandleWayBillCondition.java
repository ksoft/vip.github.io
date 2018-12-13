package com.jlt.datasync.condition;

import java.util.List;

import lombok.Data;

@Data
public class HandleWayBillCondition extends PageCondition {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 运单号集合
     */
    private List<String> waybillNos;
    /**
     * 物流单号集合
     */
    private List<String> eloNos;
    /**
     * 操作类型集合
     */
    private List<String> operationTypes;
}
