package com.jlt.datasync.biz;



import com.jlt.datasync.domain.SsmEoLogisticsOrder;

import java.util.List;

public interface SsmEoLogisticsOrderBiz {

    List<SsmEoLogisticsOrder> getUnHandleOrder();

    Integer batchUpdateByEloNo(List<String> eloNos);

    Integer batchDeleteByEloNo(List<String> eloNos);
}
