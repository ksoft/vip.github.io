package com.jlt.datasync.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlt.datasync.biz.HadleWayBillBiz;
import com.jlt.datasync.condition.HandleWayBillCondition;
import com.jlt.datasync.domain.HandleWayBill;
import com.jlt.datasync.mapper.saas.HandleWayBillMapper;

@Service
public class HandleWayBillBizImpl implements HadleWayBillBiz {
    @Autowired
    private HandleWayBillMapper handleWayBillMapper;

    @Override
    public List<HandleWayBill> getWayBillWithByCondition(HandleWayBillCondition condition) {
        return handleWayBillMapper.getWayBillWithByCondition(condition);
    }

    @Override
    public List<HandleWayBill> getWayBillEventByCondition(HandleWayBillCondition condition) {
        return handleWayBillMapper.getWayBillEventByCondition(condition);
    }

    @Override
    public Integer batchUpdateWaybillInfoExt(List<HandleWayBill> models) {
        return handleWayBillMapper.batchUpdateWaybillInfoExt(models);
    }

    @Override
    public Integer batchUpdateWaybillInfo(List<HandleWayBill> models) {
        return handleWayBillMapper.batchUpdateWaybillInfo(models);
    }

    @Override
    public Integer batchUpdateHasException() {
        return handleWayBillMapper.batchUpdateHasException();
    }

    @Override
    public List<HandleWayBill> getCanceledWaybill(HandleWayBillCondition condition) {
        return handleWayBillMapper.getCanceledWaybill(condition);
    }

    @Override
    public void batchUpdateByEloNo(List<String> eloNos) {
        handleWayBillMapper.batchUpdateByEloNo(eloNos);
    }

    @Override
    public List<HandleWayBill> getUnSetRequireTimeWaybill() {
        return handleWayBillMapper.getUnSetRequireTimeWaybill();
    }

    @Override
    public Integer  getValidWaybill(String dispatchNo) {
        return handleWayBillMapper.getValidWaybill(dispatchNo);
    }
}
