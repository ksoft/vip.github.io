package com.jlt.datasync.mapper.pms;

import com.jlt.datasync.condition.JltJobCondition;
import com.jlt.datasync.domain.JltJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Mapper
public interface JltJobMapper {
    JltJob findOne(JltJobCondition condition);
    int update(JltJob jltJob);
}
