package com.jlt.datasync.biz.impl;


import com.jlt.datasync.biz.SsmEoLogisticsOrderBiz;
import com.jlt.datasync.domain.SsmEoLogisticsOrder;
import com.jlt.datasync.mapper.saas.SsmEoLogisticsOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Service
public class SsmEoLogisticsOrderBizImpl implements SsmEoLogisticsOrderBiz {
    @Autowired
    private SsmEoLogisticsOrderMapper ssmEoLogisticsOrderMapper;

    @Override
    public List<SsmEoLogisticsOrder> getUnHandleOrder() {
        return ssmEoLogisticsOrderMapper.getUnHandleOrder();
    }

    @Override
    public Integer batchUpdateByEloNo(List<String> eloNos) {
        return ssmEoLogisticsOrderMapper.batchUpdateByEloNo(eloNos);
    }

    @Override
    public Integer batchDeleteByEloNo(List<String> eloNos) {
        return ssmEoLogisticsOrderMapper.batchDeleteByEloNo(eloNos);
    }
}
