package com.jlt.datasync.timer;

import com.alibaba.fastjson.JSON;
import com.jlt.common.baidu.DataResult;
import com.jlt.common.baidu.RouteMatrix;
import com.jlt.common.constant.Globals;
import com.jlt.common.util.BaiduMapApiUtils;
import com.jlt.datasync.biz.EtWaybillBiz;
import com.jlt.datasync.biz.JltJobBiz;
import com.jlt.datasync.condition.EtWaybillCondition;
import com.jlt.datasync.condition.JltJobCondition;
import com.jlt.datasync.domain.EtWaybill;
import com.jlt.datasync.domain.JltJob;
import com.jlt.lpm.facade.gps.GpsFacade;
import com.jlt.lpm.facade.gps.bo.GpsWtherBO;
import com.sinoservices.minima.common.util.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算运单剩余里程和预计到达时间
 *
 * @author Billy.Zhang
 */
@Component
@JobHander("jlt:waybillMileageAndTime")
public class WaybillMileageAndTimeHandleJobTask extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final Long REDES_DATA_MAX_SIZE = 10000L;

    @Autowired
    private JltJobBiz jltJobBiz;
    @Autowired
    private EtWaybillBiz etWaybillBiz;
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    private GpsFacade gpsFacade;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        logger.info("开始计算运单剩余里程和预计到达时间，定时任务");
        JltJobCondition jltJobCondition = new JltJobCondition();
        jltJobCondition.setId(1L);
        JltJob jltJob = jltJobBiz.findOne(jltJobCondition);
        String redisKey = Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + jltJob.getTenantId();
        Long size = redisTemplate.opsForList().size(redisKey);
        if (size > REDES_DATA_MAX_SIZE) {
            logger.error("[" + redisKey + "] redis 数据量超过：" + REDES_DATA_MAX_SIZE + ",请确认消费端程序是否运行或存在异常，本次任务退出");
            return ReturnT.FAIL;
        }

        //查找在途运单
        EtWaybillCondition etWaybillCondition = new EtWaybillCondition();
        etWaybillCondition.setTenantId(jltJob.getTenantId());
        List<EtWaybill> waybillList = etWaybillBiz.findOnWayList(etWaybillCondition);
        if (CollectionUtils.isEmpty(waybillList)) {
            logger.info("在途运单数量为0，本次任务退出");
            return ReturnT.SUCCESS;
        }
        logger.info("在途运单数:" + waybillList.size());
        Date now = new Date();
        String nowStr = DateUtil.format(now);
        for (EtWaybill etWaybill : waybillList) {
            try {
                List<String> carNos = new ArrayList<>();
                carNos.add(etWaybill.getVehicleNumber());
                Map<String, GpsWtherBO> gpsWtherBOMap = gpsFacade.getGpsWtherByCarNo(carNos);
                GpsWtherBO gpsWtherBO = gpsWtherBOMap.get(etWaybill.getVehicleNumber());
                if (gpsWtherBO != null && gpsWtherBO.getLon() != null && gpsWtherBO.getLat() != null) {
                    String oriLat = gpsWtherBO.getLat().toString();
                    String oriLng = gpsWtherBO.getLon().toString();
                    Map<String, String> desLatLng = BaiduMapApiUtils.getLngAndLat(etWaybill.getDestinationAddress());
                    RouteMatrix routeMatrix =
                            BaiduMapApiUtils.getRouteMatrixFromLngAndLat(oriLng, oriLat, desLatLng.get("lng"), desLatLng.get("lat"));
                    logger.info(JSON.toJSONString(routeMatrix));
                    if (routeMatrix.getStatus() == 0) {
                        DataResult dataResult = routeMatrix.getResult().get(0);
                        Map<String, Object> dataMap = new HashMap<>();
                        dataMap.put("tenant_id", etWaybill.getTenantId());
                        dataMap.put("waybill_no", etWaybill.getWaybillNo());
                        dataMap.put("surplus_mileage", new BigDecimal(dataResult.getDistance().getValue()).divide(BigDecimal.valueOf(1000))
                                .setScale(2, BigDecimal.ROUND_HALF_UP));//转换成公里
                        dataMap.put("predict_arrival_time",
                                DateUtil.format(DateUtils.addMinutes(now, Integer.parseInt(dataResult.getDuration().getValue()))));
                        dataMap.put("creator", "定时器");
                        dataMap.put("create_name", "定时器");
                        dataMap.put("create_time", nowStr);
                        dataMap.put("modifier", "定时器");
                        dataMap.put("modify_name", "定时器");
                        dataMap.put("modify_time", nowStr);
                        redisTemplate.opsForList().leftPush(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + jltJob.getTenantId(), dataMap);
                    }
                }
                logger.info("end:" + etWaybill.getVehicleNumber());
            } catch (Exception e) {
                logger.error("运单：【" + etWaybill.getWaybillNo() + "】，计算剩余里程失败：" + e.getMessage());
            }
        }
        logger.info("计算运单剩余里程和预计到达时间定时任务，执行成功");
        return ReturnT.SUCCESS;
    }
}
