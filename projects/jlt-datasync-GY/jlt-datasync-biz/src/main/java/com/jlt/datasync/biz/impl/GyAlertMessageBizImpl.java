package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.GyAlertMessageBiz;
import com.jlt.datasync.condition.GyAlertMessageCondition;
import com.jlt.datasync.domain.GyAlertMessage;
import com.jlt.datasync.dto.GyAlertMessageDto;
import com.jlt.datasync.mapper.saas.GyAlertMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GyAlertMessageBizImpl implements GyAlertMessageBiz {


    @Autowired
    private GyAlertMessageMapper gyAlertMessageMapper;

    @Override
    public List<GyAlertMessageDto> findByCondition(GyAlertMessageCondition condition) {
        return gyAlertMessageMapper.findByCondition(condition);
    }

    @Override
    public Integer insert(GyAlertMessage model) {
        return gyAlertMessageMapper.insert(model);
    }

    @Override
    public Integer update(GyAlertMessage model) {
        return gyAlertMessageMapper.update(model);
    }

    @Override
    public Integer batchInsert(List<GyAlertMessage> models) {
        return gyAlertMessageMapper.batchInsert(models);
    }

    @Override
    public Integer batchUpdate(List<GyAlertMessage> models) {
        return gyAlertMessageMapper.batchUpdate(models);
    }

    @Override
    public Integer deleteByCondition(GyAlertMessageCondition condition) {
        return gyAlertMessageMapper.deleteByCondition(condition);
    }

}
