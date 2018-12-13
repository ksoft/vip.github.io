package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.GyReturnPrescriptionBiz;
import com.jlt.datasync.condition.GyReturnPrescriptionCondition;
import com.jlt.datasync.domain.GyReturnPrescription;
import com.jlt.datasync.dto.GyReturnPrescriptionDto;
import com.jlt.datasync.mapper.saas.GyReturnPrescriptionMapper;
import com.sinoservices.common.other.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class GyReturnPrescriptionBizImpl implements GyReturnPrescriptionBiz {

    @Autowired
    private GyReturnPrescriptionMapper gyReturnPrescriptionMapper;


    @Override
    public List<GyReturnPrescriptionDto> findByCondition(GyReturnPrescriptionCondition condition) {
        return gyReturnPrescriptionMapper.findByCondition(condition);
    }

    @Override
    public Integer insert(GyReturnPrescription model) {
        return gyReturnPrescriptionMapper.insert(model);
    }

    @Override
    public Integer update(GyReturnPrescription model) {
        return gyReturnPrescriptionMapper.update(model);
    }

    @Override
    public Integer batchInsert(List<GyReturnPrescription> models) {
        return gyReturnPrescriptionMapper.batchInsert(models);
    }

    @Override
    public Integer batchUpdate(List<GyReturnPrescription> models) {
        return gyReturnPrescriptionMapper.batchUpdate(models);
    }

    @Override
    public Integer deleteByCondition(GyReturnPrescriptionCondition condition) {
        return gyReturnPrescriptionMapper.deleteByCondition(condition);
    }

    @Override
    public Integer insertAndDeleteByIds(List<GyReturnPrescription> returnList, List<GyReturnPrescription> updateList, List<Long> ids) {
        if (EmptyUtils.isNotEmpty(ids)) {
            GyReturnPrescriptionCondition condition = new GyReturnPrescriptionCondition();
            condition.setIds(ids);
            this.deleteByCondition(condition);
        }
        if (EmptyUtils.isNotEmpty(returnList)) {
            this.batchInsert(returnList);
        }
        if (EmptyUtils.isNotEmpty(updateList)) {
            this.batchUpdate(updateList);
        }
        return 0;
    }

}
