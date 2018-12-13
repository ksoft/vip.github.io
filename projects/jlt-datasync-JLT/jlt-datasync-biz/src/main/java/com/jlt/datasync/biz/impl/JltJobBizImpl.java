package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.JltJobBiz;
import com.jlt.datasync.condition.JltJobCondition;
import com.jlt.datasync.domain.JltJob;
import com.jlt.datasync.mapper.pms.JltJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Service
public class JltJobBizImpl implements JltJobBiz {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JltJobMapper jltJobMapper;

    @Override
    public JltJob findOne(JltJobCondition condition) {
        return jltJobMapper.findOne(condition);
    }

    public int update(JltJob jltJob) {
        return jltJobMapper.update(jltJob);
    }
}
