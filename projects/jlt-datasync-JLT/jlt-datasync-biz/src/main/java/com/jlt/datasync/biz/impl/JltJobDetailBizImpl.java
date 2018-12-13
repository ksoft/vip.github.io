package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.JltJobDetailBiz;
import com.jlt.datasync.condition.JltJobDetailCondition;
import com.jlt.datasync.domain.JltJobDetail;
import com.jlt.datasync.mapper.pms.JltJobDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Service
public class JltJobDetailBizImpl implements JltJobDetailBiz {
    @Autowired
    private JltJobDetailMapper jltJobDetailMapper;

    @Override
    public List<JltJobDetail> findList(JltJobDetailCondition condition) {
        return jltJobDetailMapper.findList(condition);
    }

    @Override
    public JltJobDetail findOne(JltJobDetailCondition condition) {
        return jltJobDetailMapper.findOne(condition);
    }

    @Override
    public int update(JltJobDetail jltJobDetail) {
        return jltJobDetailMapper.update(jltJobDetail);
    }
}
