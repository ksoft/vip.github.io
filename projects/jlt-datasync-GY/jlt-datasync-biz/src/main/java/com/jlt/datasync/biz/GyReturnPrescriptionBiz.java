package com.jlt.datasync.biz;



import com.jlt.datasync.condition.GyReturnPrescriptionCondition;
import com.jlt.datasync.domain.GyReturnPrescription;
import com.jlt.datasync.dto.GyReturnPrescriptionDto;

import java.util.List;

public interface GyReturnPrescriptionBiz {

    /**
     * 不分页查询
     * 
     * @param condition
     * @return
     */
    List<GyReturnPrescriptionDto> findByCondition(GyReturnPrescriptionCondition condition);

    /**
     * 新增方法
     * 
     * @param model
     * @return
     */
    Integer insert(GyReturnPrescription model);

    /**
     * 修改方法
     * 
     * @param model
     * @return
     */
    Integer update(GyReturnPrescription model);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    Integer batchInsert(List<GyReturnPrescription> models);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    Integer batchUpdate(List<GyReturnPrescription> models);


    /**
     * 
     * 根据条件删除
     * 
     * @param condition
     * @return
     */
    Integer deleteByCondition(GyReturnPrescriptionCondition condition);

    /**
     * 删除以及添加信息
     * 
     * @param returnList
     * @param ids
     * @return
     */
    Integer insertAndDeleteByIds(List<GyReturnPrescription> returnList, List<GyReturnPrescription> updateList, List<Long> ids);
}
