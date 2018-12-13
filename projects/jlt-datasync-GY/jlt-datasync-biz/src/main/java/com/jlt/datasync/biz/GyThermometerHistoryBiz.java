package com.jlt.datasync.biz;

import com.jlt.datasync.condition.GyThermometerHistoryCondition;
import com.jlt.datasync.domain.GyThermometerHistory;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/11/3
 */
public interface GyThermometerHistoryBiz {
    List<GyThermometerHistory> findList(GyThermometerHistoryCondition condition);
    Integer batchUpdate(List<GyThermometerHistory> models);
}
