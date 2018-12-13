package com.jlt.datasync.biz.impl;

import com.jlt.datasync.biz.EtWaybillBiz;
import com.jlt.datasync.condition.EtWaybillCondition;
import com.jlt.datasync.domain.EtWaybill;
import com.jlt.datasync.mapper.saas.EtWaybillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/10/13
 */
@Service
public class EtWaybillBizImpl implements EtWaybillBiz{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EtWaybillMapper etWaybillMapper;

    @Override
    public List<EtWaybill> findOnWayList(EtWaybillCondition condition) {
        return etWaybillMapper.findOnWayList(condition);
    }
}
