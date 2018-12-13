import com.alibaba.fastjson.JSONObject;
import com.jlt.common.constant.Globals;
import com.jlt.common.enums.YesNo;
import com.jlt.datasync.biz.MonAlertRuleBiz;
import com.jlt.datasync.domain.MonAlertRule;
import com.jlt.datasync.dto.DataTransferDto;
import com.jlt.startup.DataSyncWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author Billy.Zhang
 * @date 2018/10/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataSyncWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class RedisTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate2;
    @Autowired
    private MonAlertRuleBiz monAlertRuleBiz;

    @Test
    public void testRedis() {
        String key = "mon:order:alert:tenant:222222";
        Map<String, String> map = new HashMap<>();
        map.put(Globals.TEMPERATURE_CYCLE_ALERT, "412");
        map.put(Globals.TEMPERATURE_DATA_ALERT, "1235");
        String aa = JSONObject.toJSONString(map);
        redisTemplate.opsForHash().put(key, Globals.TEMPERATURE_EXCEEDED_ALARM, aa);
    }

    @Test
    public void testMonAlertRule() {
        Long tenantId = 1044404801560776705L;
        try {
            monAlertRuleBiz.refreshMonAlertRuleCache(tenantId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(111);
    }

    @Test
    public void testA() {
        Map<Object, Object> map444 = redisTemplate2.opsForHash().entries("waybill:mileage");

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("waybill_no", "aaa");
        dataMap.put("surplus_mileage", "130米");
        dataMap.put("predict_arrival_time", "2018-12-12 11:10:23");
        redisTemplate2.opsForHash().put(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX, "aaa", dataMap);
        redisTemplate2.opsForList().leftPush(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt", dataMap);

        Map<String, Object> dataMap2 = new HashMap<>();
        dataMap2.put("waybill_no", "bbb");
        dataMap2.put("surplus_mileage", "140米");
        dataMap2.put("predict_arrival_time", "2018-12-12 11:10:23");
        redisTemplate2.opsForHash().put(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX, "bbb", dataMap2);
        redisTemplate2.opsForList().leftPush(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt", dataMap2);

        Map<String, Object> dataMap3 = new HashMap<>();
        dataMap3.put("waybill_no", "cc");
        dataMap3.put("surplus_mileage", "15550米");
        dataMap3.put("predict_arrival_time", "2018-12-12 11:10:23");
        /*List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(dataMap);
        dataList.add(dataMap2);*/
        redisTemplate2.opsForHash().put(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX, "ccc", dataMap3);
        redisTemplate2.opsForList().leftPush(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt", dataMap3);

        Long size = redisTemplate2.opsForList().size(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt");
        Map<String, Object> mapaas =
                (Map<String, Object>) redisTemplate2.opsForList().rightPop(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt");
        Long size1 = redisTemplate2.opsForList().size(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt");
        Map<String, Object> mapaas2 =
                (Map<String, Object>) redisTemplate2.opsForList().rightPop(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt");
        Long size3 = redisTemplate2.opsForList().size(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt");
        Map<String, Object> mapaas3 =
                (Map<String, Object>) redisTemplate2.opsForList().rightPop(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt");
        Long size4 = redisTemplate2.opsForList().size(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX + "ttt");

        Map<Object, Object> map = redisTemplate2.opsForHash().entries(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX);
        DataTransferDto dataTransferDto = new DataTransferDto();
        Map<String, List<Map<String, Object>>> ss = new HashMap<>();
        List<Map<String, Object>> ll = new ArrayList<>();
        for (Object o : map.values()) {
            Map<String, Object> mm = (Map<String, Object>) o;
            ll.add(mm);
            redisTemplate2.opsForHash().delete(Globals.WAYBILL_MILEAGE_REDIS_KEY_PREFIX, "bbb");
        }
        ss.put("et_waybill_mileage", ll);
        dataTransferDto.setSaasDataMap(ss);

    }
}
