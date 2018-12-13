package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.GyjlDispatchOrderBiz;
import com.jlt.datasync.domain.GyjlDispatchOrder;
import com.jlt.datasync.mapper.saas.GyjlDispatchOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GyjlDispatchOrderBizImpl implements GyjlDispatchOrderBiz {

    @Autowired
    private GyjlDispatchOrderMapper gyjlDispatchOrderMapper;

    @Override
    public Integer bacthDeleteByEcNo(List<String> exNos) {
        return gyjlDispatchOrderMapper.bacthDeleteByEcNo(exNos);
    }

    @Override
    public Integer bacthUpdateByEcNo(List<String> exNos) {
        return gyjlDispatchOrderMapper.bacthUpdateByEcNo(exNos);
    }

    @Override
    public Integer batchUpdateWayBillDetail(List<String> waybillNos) {
        return gyjlDispatchOrderMapper.batchUpdateWayBillDetail(waybillNos);
    }

    @Override
    public Integer batchInsert(List<GyjlDispatchOrder> models) {
        return gyjlDispatchOrderMapper.batchInsert(models);
    }

    @Override
    public GyjlDispatchOrder findByOwbEcNo(String owbEcNo) {
        return gyjlDispatchOrderMapper.findByOwbEcNo(owbEcNo);
    }

    @Override
    public Integer batchUpdate(List<GyjlDispatchOrder> models) {
        return gyjlDispatchOrderMapper.batchUpdate(models);
    }

    @Override
    public Integer batchMerge(List<GyjlDispatchOrder> models) {
        return gyjlDispatchOrderMapper.batchMerge(models);
    }
}
