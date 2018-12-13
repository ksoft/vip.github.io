package com.jlt.datasync.mapper.saas;


import java.util.List;

import com.jlt.datasync.condition.HandleWayBillCondition;
import com.jlt.datasync.domain.HandleWayBill;

public interface HandleWayBillMapper {
    /**
     * 获取运单的集合
     */
    List<HandleWayBill> getWayBillWithByCondition(HandleWayBillCondition condition);

    List<HandleWayBill> getWayBillEventByCondition(HandleWayBillCondition condition);

    Integer batchUpdateWaybillInfoExt(List<HandleWayBill> models);

    Integer batchUpdateWaybillInfo(List<HandleWayBill> models);

    Integer batchUpdateHasException();

    List<HandleWayBill> getCanceledWaybill(HandleWayBillCondition condition);

    void batchUpdateByEloNo(List<String> eloNos);

    List<HandleWayBill> getUnSetRequireTimeWaybill();

    /**
     * 根据派车单，获取有效的运单
     * @return
     */
    Integer getValidWaybill(String dispatchNo);
}
