package com.jlt.datasync.mapper.saas;

import com.jlt.datasync.condition.GyPrescriptionSettingCondition;
import com.jlt.datasync.domain.GyPrescriptionSetting;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/11/2
 */
public interface GyPrescriptionSettingMapper {
    List<GyPrescriptionSetting> findList(GyPrescriptionSettingCondition condition);
}
