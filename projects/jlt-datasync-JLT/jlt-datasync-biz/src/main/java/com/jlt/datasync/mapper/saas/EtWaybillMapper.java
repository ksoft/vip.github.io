package com.jlt.datasync.mapper.saas;

import com.jlt.datasync.condition.EtWaybillCondition;
import com.jlt.datasync.domain.EtWaybill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Mapper
public interface EtWaybillMapper {
    List<EtWaybill> findOnWayList(EtWaybillCondition condition);
}
