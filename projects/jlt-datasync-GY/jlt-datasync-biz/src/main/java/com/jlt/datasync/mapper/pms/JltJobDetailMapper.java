package com.jlt.datasync.mapper.pms;

import com.jlt.datasync.condition.JltJobDetailCondition;
import com.jlt.datasync.domain.JltJobDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Mapper
public interface JltJobDetailMapper {
    JltJobDetail findOne(JltJobDetailCondition condition);
    List<JltJobDetail> findList(JltJobDetailCondition condition);
    int update(JltJobDetail jltJobDetail);
}
