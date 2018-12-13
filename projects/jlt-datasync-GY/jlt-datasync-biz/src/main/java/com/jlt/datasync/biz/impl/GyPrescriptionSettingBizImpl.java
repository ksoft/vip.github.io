package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.GyPrescriptionSettingBiz;
import com.jlt.datasync.condition.GyPrescriptionSettingCondition;
import com.jlt.datasync.domain.GyPrescriptionSetting;
import com.jlt.datasync.mapper.saas.GyPrescriptionSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/11/2
 */
@Service
public class GyPrescriptionSettingBizImpl implements GyPrescriptionSettingBiz{

    @Autowired
    private GyPrescriptionSettingMapper gyPrescriptionSettingMapper;

    @Override
    public List<GyPrescriptionSetting> findList(GyPrescriptionSettingCondition condition) {
        return gyPrescriptionSettingMapper.findList(condition);
    }
}
