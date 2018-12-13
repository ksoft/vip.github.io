package com.jlt.datasync.mapper.saas;



import com.jlt.datasync.condition.GyAlertMessageCondition;
import com.jlt.datasync.domain.GyAlertMessage;
import com.jlt.datasync.dto.GyAlertMessageDto;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface GyAlertMessageMapper {
    /**
     * 分页查询方法
     * 
     * @param example
     * @param bounds
     * @return
     */
    public List<GyAlertMessageDto> findByCondition(GyAlertMessageCondition condition, RowBounds bounds);

    /**
     * 不分页查询
     * 
     * @param condition
     * @return
     */
    public List<GyAlertMessageDto> findByCondition(GyAlertMessageCondition condition);

    /**
     * 新增方法
     * 
     * @param model
     * @return
     */
    public Integer insert(GyAlertMessage model);

    /**
     * 修改方法
     * 
     * @param model
     * @return
     */
    public Integer update(GyAlertMessage model);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    public Integer batchInsert(List<GyAlertMessage> models);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    public Integer batchUpdate(List<GyAlertMessage> models);

    /**
     * 根据条件删除方法
     * 
     * @param example
     * @param bounds
     * @return
     */
    public Integer deleteByCondition(GyAlertMessageCondition condition);

}
