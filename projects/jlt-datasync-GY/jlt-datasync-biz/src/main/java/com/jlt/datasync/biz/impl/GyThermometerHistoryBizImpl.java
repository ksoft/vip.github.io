package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.GyThermometerHistoryBiz;
import com.jlt.datasync.condition.GyThermometerHistoryCondition;
import com.jlt.datasync.domain.GyThermometerHistory;
import com.jlt.datasync.mapper.saas.GyThermometerHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/11/3
 */
@Service
public class GyThermometerHistoryBizImpl implements GyThermometerHistoryBiz {
    @Autowired
    private GyThermometerHistoryMapper gyThermometerHistoryMapper;

    @Override
    public List<GyThermometerHistory> findList(GyThermometerHistoryCondition condition) {
        return gyThermometerHistoryMapper.findList(condition);
    }

    @Override
    public Integer batchUpdate(List<GyThermometerHistory> models) {
        return gyThermometerHistoryMapper.batchUpdate(models);
    }
}
