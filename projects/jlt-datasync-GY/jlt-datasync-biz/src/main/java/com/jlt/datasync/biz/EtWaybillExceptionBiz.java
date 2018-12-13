package com.jlt.datasync.biz;

import com.jlt.datasync.condition.EtWaybillExceptionCondition;
import com.jlt.datasync.domain.EtWaybillException;
import com.jlt.datasync.dto.EtWaybillExceptionDto;

import java.util.List;


public interface EtWaybillExceptionBiz {

    /**
     * 根据条件查询异常信息
     *
     * @param condition
     * @return
     */
    List<EtWaybillExceptionDto> getExceptionByCondition(EtWaybillExceptionCondition condition);

    /**
     * 批量更新
     *
     * @param list
     */
    void batchUpdateGyExceptionReturnTime(List<EtWaybillExceptionDto> list);
    /**
     * 批量更新是否已预报警
     * @param list
     */
    void batchUpdateIsWarningOrAlarm(List<EtWaybillExceptionDto> list);

    /**
     * 批量更新上一次通知人
     * @param list
     */
    void batchUpdateLastNoticeSettingId(List<EtWaybillExceptionDto> list);

    /**
     * 根据公司查询异常处理
     * */
    List<EtWaybillExceptionDto> getExceptions();

    /**
     * 批量插入
     * @param models
     * @return
     */
    Integer batchInsert(List<EtWaybillException> models);

    /**
     * 获取需要退货预报警的异常数据
     * @param condition
     * @return
     */
    List<EtWaybillExceptionDto> findReturnList(EtWaybillExceptionCondition condition);

    /**
     * 获取需要温度预报警通知的异常数据
     * @param condition
     * @return
     */
    List<EtWaybillExceptionDto> findTemperatureList(EtWaybillExceptionCondition condition);
    /**
     * 获取被拒收的异常
     * @param condition
     * @return
     */
    List<EtWaybillExceptionDto> findRejectException(EtWaybillExceptionCondition condition);
}
