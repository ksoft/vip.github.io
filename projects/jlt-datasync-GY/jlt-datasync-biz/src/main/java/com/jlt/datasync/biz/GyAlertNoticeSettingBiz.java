package com.jlt.datasync.biz;

import com.jlt.datasync.condition.GyAlertNoticeSettingCondition;
import com.jlt.datasync.dto.GyAlertNoticeSettingDto;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/11/20
 */
public interface GyAlertNoticeSettingBiz {
    /**
     * 获取列表
     * @param condition
     * @return
     */
    List<GyAlertNoticeSettingDto> findList(GyAlertNoticeSettingCondition condition);
}
