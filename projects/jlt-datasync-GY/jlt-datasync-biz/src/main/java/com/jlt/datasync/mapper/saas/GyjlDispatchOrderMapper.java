package com.jlt.datasync.mapper.saas;

import com.jlt.datasync.domain.GyjlDispatchOrder;

import java.util.List;

public interface GyjlDispatchOrderMapper {

    Integer bacthDeleteByEcNo(List<String> exNos);

    Integer bacthUpdateByEcNo(List<String> exNos);

    Integer batchUpdateWayBillDetail(List<String> waybillNos);

    /**
     * 批量新增方法
     *
     * @param models
     * @return
     */
    Integer batchInsert(List<GyjlDispatchOrder> models);

    /**
     * 主键查询
     *
     * @param owbEcNo
     * @return
     */
    GyjlDispatchOrder findByOwbEcNo(String owbEcNo);

    /**
     * 批量更新方法
     *
     * @param models
     * @return
     */
    Integer batchUpdate(List<GyjlDispatchOrder> models);

    /**
     * 批量Merge
     *
     * @param models
     * @return
     */
    Integer batchMerge(List<GyjlDispatchOrder> models);
}
