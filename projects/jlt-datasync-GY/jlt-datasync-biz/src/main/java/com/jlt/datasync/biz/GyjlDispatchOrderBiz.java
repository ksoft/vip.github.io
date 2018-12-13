package com.jlt.datasync.biz;

import com.jlt.datasync.domain.GyjlDispatchOrder;
import com.jlt.datasync.dto.GyjlDispatchOrderDto;

import java.util.List;

public interface GyjlDispatchOrderBiz {

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
     * 根据主键查询
     *
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
