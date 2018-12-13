package com.jlt.datasync.timer;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jlt.datasync.biz.GyjlDispatchOrderBiz;
import com.jlt.datasync.biz.HadleWayBillBiz;
import com.jlt.datasync.biz.SsmEoLogisticsOrderBiz;
import com.jlt.datasync.condition.HandleWayBillCondition;
import com.jlt.datasync.domain.HandleWayBill;
import com.jlt.datasync.domain.SsmEoLogisticsOrder;
import com.sinoservices.minima.common.util.EmptyUtil;
import com.sinoservices.xframework.core.utils.EmptyUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;

/**
 * 删除被取消的派车单
 *
 * @author Billy.Zhang
 * @date 2018/10/11
 */
@Component
@JobHander("gy:dispatchcancel")
public class GyDispatchCancleHandleJobTask extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private GyjlDispatchOrderBiz gyjlDispatchOrderManager;
    @Autowired
    private HadleWayBillBiz billManager;
    @Autowired
    private SsmEoLogisticsOrderBiz ssmEoLogisticsOrderBiz;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("开始删除被取消的派车单，定时任务");
        dataHandleTaskEntrance();
        logger.info("删除被取消的派车单定时任务，执行成功");
        return ReturnT.SUCCESS;
    }

    /**
     * 处理数据的入口
     */
    private void dataHandleTaskEntrance() {
        getWaybillData();
    }

    /**
     * 获取所有被取消的运单
     */
    private void getWaybillData() {
        List<SsmEoLogisticsOrder> ssmEoLogisticsOrderList = ssmEoLogisticsOrderBiz.getUnHandleOrder();
        if (EmptyUtil.isEmpty(ssmEoLogisticsOrderList)) {
            return;
        }
        List<String> eloNos = ssmEoLogisticsOrderList.stream().map(SsmEoLogisticsOrder::getEloNo).collect(Collectors.toList());
        HandleWayBillCondition condition = new HandleWayBillCondition();
        condition.setEloNos(eloNos);
        List<HandleWayBill> billModels = billManager.getCanceledWaybill(condition);
        if (EmptyUtils.isEmpty(billModels)) {
            return;
        }
        List<String> ecNos = billModels.stream().map(HandleWayBill::getOwbEcNo).collect(Collectors.toList());
        // 封装调度单数据
        packageDispatchData(eloNos, ecNos);
    }

    /**
     * 封装调度单数据
     *
     * @param eloNos
     * @param ecNos
     */
    private void packageDispatchData(List<String> eloNos, List<String> ecNos) {
        gyjlDispatchOrderManager.bacthUpdateByEcNo(ecNos);
        ssmEoLogisticsOrderBiz.batchUpdateByEloNo(eloNos);
        billManager.batchUpdateByEloNo(eloNos);
    }
}
