package com.jlt.datasync.biz;

import com.jlt.datasync.condition.EtWaybillCondition;
import com.jlt.datasync.domain.EtWaybill;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/10/12
 */
public interface EtWaybillBiz {
    List<EtWaybill> findOnWayList(EtWaybillCondition condition);
}
