package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.EtWaybillExceptionBiz;
import com.jlt.datasync.condition.EtWaybillExceptionCondition;
import com.jlt.datasync.domain.EtWaybillException;
import com.jlt.datasync.dto.EtWaybillExceptionDto;
import com.jlt.datasync.mapper.saas.EtWaybillExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class EtWaybillExceptionBizImpl implements EtWaybillExceptionBiz {

    @Autowired
    private EtWaybillExceptionMapper etWaybillExceptionMapper;

    @Override
    public List<EtWaybillExceptionDto> getExceptionByCondition(EtWaybillExceptionCondition condition) {
        return etWaybillExceptionMapper.getExceptionByCondition(condition);
    }

    @Override
    public void batchUpdateGyExceptionReturnTime(List<EtWaybillExceptionDto> list) {
        etWaybillExceptionMapper.batchUpdateGyExceptionReturnTime(list);
    }

    @Override
    public void batchUpdateIsWarningOrAlarm(List<EtWaybillExceptionDto> list) {
        etWaybillExceptionMapper.batchUpdateIsWarningOrAlarm(list);
    }

    @Override
    public void batchUpdateLastNoticeSettingId(List<EtWaybillExceptionDto> list) {
        etWaybillExceptionMapper.batchUpdateLastNoticeSettingId(list);
    }

    @Override
    public List<EtWaybillExceptionDto> getExceptions() {
        return etWaybillExceptionMapper.getExceptions();
    }

    @Override
    public Integer batchInsert(List<EtWaybillException> models) {
        return etWaybillExceptionMapper.batchInsert(models);
    }

    @Override
    public List<EtWaybillExceptionDto> findReturnList(EtWaybillExceptionCondition condition) {
        return etWaybillExceptionMapper.findReturnList(condition);
    }

    @Override
    public List<EtWaybillExceptionDto> findTemperatureList(EtWaybillExceptionCondition condition) {
        return etWaybillExceptionMapper.findTemperatureList(condition);
    }

    @Override
    public List<EtWaybillExceptionDto> findRejectException(EtWaybillExceptionCondition condition) {
        return etWaybillExceptionMapper.findRejectException(condition);
    }
}
