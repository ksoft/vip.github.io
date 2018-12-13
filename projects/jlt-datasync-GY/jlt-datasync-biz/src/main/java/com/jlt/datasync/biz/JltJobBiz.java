package com.jlt.datasync.biz;

import com.jlt.datasync.condition.JltJobCondition;
import com.jlt.datasync.domain.JltJob;



/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
public interface JltJobBiz {
    JltJob findOne(JltJobCondition condition);

    int update(JltJob condition);
}
