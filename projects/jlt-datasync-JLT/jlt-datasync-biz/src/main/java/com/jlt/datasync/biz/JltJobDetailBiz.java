package com.jlt.datasync.biz;

import com.jlt.common.enums.DataSchema;
import com.jlt.common.enums.JobType;
import com.jlt.common.enums.YesNo;
import com.jlt.datasync.condition.JltJobDetailCondition;
import com.jlt.datasync.domain.JltJobDetail;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public interface JltJobDetailBiz {
    List<JltJobDetail> findList(JltJobDetailCondition condition);

    JltJobDetail findOne(JltJobDetailCondition condition);

    int update(JltJobDetail jltJobDetail);
}
