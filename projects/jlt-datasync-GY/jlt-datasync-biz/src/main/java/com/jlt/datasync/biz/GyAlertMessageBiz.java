package com.jlt.datasync.biz;



import com.jlt.datasync.condition.GyAlertMessageCondition;
import com.jlt.datasync.domain.GyAlertMessage;
import com.jlt.datasync.dto.GyAlertMessageDto;

import java.util.List;

public interface GyAlertMessageBiz {


    /**
     * 不分页查询
     * 
     * @param condition
     * @return
     */
    List<GyAlertMessageDto> findByCondition(GyAlertMessageCondition condition);

    /**
     * 新增方法
     * 
     * @param model
     * @return
     */
    Integer insert(GyAlertMessage model);

    /**
     * 修改方法
     * 
     * @param model
     * @return
     */
    Integer update(GyAlertMessage model);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    Integer batchInsert(List<GyAlertMessage> models);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    Integer batchUpdate(List<GyAlertMessage> models);


    /**
     * 
     * 根据条件删除
     * 
     * @param condition
     * @return
     */
    Integer deleteByCondition(GyAlertMessageCondition condition);


}
