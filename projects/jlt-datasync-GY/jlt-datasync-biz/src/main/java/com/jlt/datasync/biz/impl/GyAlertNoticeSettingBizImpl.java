package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.GyAlertNoticeSettingBiz;
import com.jlt.datasync.condition.GyAlertNoticeSettingCondition;
import com.jlt.datasync.dto.GyAlertNoticeSettingDto;
import com.jlt.datasync.mapper.saas.GyAlertNoticeSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/11/20
 */
@Service
public class GyAlertNoticeSettingBizImpl implements GyAlertNoticeSettingBiz {
    @Autowired
    private GyAlertNoticeSettingMapper gyAlertNoticeSettingMapper;

    @Override
    public List<GyAlertNoticeSettingDto> findList(GyAlertNoticeSettingCondition condition) {
        return gyAlertNoticeSettingMapper.findList(condition);
    }
}
