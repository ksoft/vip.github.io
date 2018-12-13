package com.jlt.datasync.mapper.saas;



import com.jlt.datasync.condition.GyReturnPrescriptionCondition;
import com.jlt.datasync.domain.GyReturnPrescription;
import com.jlt.datasync.dto.GyReturnPrescriptionDto;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface GyReturnPrescriptionMapper {
    /**
     * 分页查询方法
     * 
     * @param example
     * @param bounds
     * @return
     */
    public List<GyReturnPrescriptionDto> findByCondition(GyReturnPrescriptionCondition condition, RowBounds bounds);

    /**
     * 不分页查询
     * 
     * @param condition
     * @return
     */
    public List<GyReturnPrescriptionDto> findByCondition(GyReturnPrescriptionCondition condition);

    /**
     * 新增方法
     * 
     * @param model
     * @return
     */
    public Integer insert(GyReturnPrescription model);

    /**
     * 修改方法
     * 
     * @param model
     * @return
     */
    public Integer update(GyReturnPrescription model);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    public Integer batchInsert(List<GyReturnPrescription> models);


    /**
     * 批量新增方法
     * 
     * @param models
     * @return
     */
    public Integer batchUpdate(List<GyReturnPrescription> models);

    /**
     * 根据条件删除方法
     * 
     * @param example
     * @param bounds
     * @return
     */
    public Integer deleteByCondition(GyReturnPrescriptionCondition condition);
}
