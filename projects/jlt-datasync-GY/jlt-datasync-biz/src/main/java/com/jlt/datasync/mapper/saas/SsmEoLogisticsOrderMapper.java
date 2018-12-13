package com.jlt.datasync.mapper.saas;



import com.jlt.datasync.domain.SsmEoLogisticsOrder;

import java.util.List;

public interface SsmEoLogisticsOrderMapper {
    /**
     * 查询列表
     * @return
     */
    List<SsmEoLogisticsOrder> getUnHandleOrder();

    Integer batchUpdateByEloNo(List<String> eloNos);

    Integer batchDeleteByEloNo(List<String> eloNos);
}
