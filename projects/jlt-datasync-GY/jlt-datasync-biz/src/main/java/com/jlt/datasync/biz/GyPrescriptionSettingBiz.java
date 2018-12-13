package com.jlt.datasync.biz;

import com.jlt.datasync.condition.GyPrescriptionSettingCondition;
import com.jlt.datasync.domain.GyPrescriptionSetting;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/11/2
 */
public interface GyPrescriptionSettingBiz {
    List<GyPrescriptionSetting> findList(GyPrescriptionSettingCondition condition);
}
