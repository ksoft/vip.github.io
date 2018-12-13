package com.jlt.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlt.common.baidu.RouteMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author George.Lai
 */
public class BaiduMapApiUtils {
    static Logger logger = LoggerFactory.getLogger(BaiduMapApiUtils.class);
    private static final String GET_LNG_LAT_URL = " http://api.map.baidu.com/geocoder/v2/";
    private static final String GET_DISTANCE_URL = "http://api.map.baidu.com/routematrix/v2/driving";
    private static final String KEY = "2a8432fa52e468d4644b16afa735618e";// 晶链通公共Key,重新申请Key后请更新此处
    private static final String TURE = "ture";
    private static final String FALSE = "false";
    private static final String STATUS = "status";

    private BaiduMapApiUtils() {

    }

    /**
     * 根据输入的地点获取对应经纬度
     *
     * @param place
     * @return
     */
    public static Map<String, String> getLngAndLat(String place) {
        Map<String, String> lngAndLatMap = new HashMap<>();
        lngAndLatMap.put(STATUS, FALSE);
        lngAndLatMap.put("lng", null);
        lngAndLatMap.put("lat", null);

        StringBuilder url = new StringBuilder();
        url.append(GET_LNG_LAT_URL).append("?address=").append(place).append("&output=json").append("&ak=").append(KEY);
        String response = HttpTool.httpGet(url.toString());
        JSONObject responseObject = JSON.parseObject(response);
        if (responseObject.getInteger(STATUS) == 0) {
            JSONObject resultObject = JSON.parseObject(responseObject.getString("result"));
            JSONObject locationObject = JSON.parseObject(resultObject.getString("location"));
            lngAndLatMap.put(STATUS, TURE);
            lngAndLatMap.put("lng", locationObject.getString("lng"));
            lngAndLatMap.put("lat", locationObject.getString("lat"));
        }
        return lngAndLatMap;
    }

    /**
     * 根据起点与终点的经纬度计算距离和时间(驾车模式)
     *
     * @param originLng
     * @param originLat
     * @param destinationLng
     * @param destinationLat
     * @return
     */
    public static RouteMatrix getRouteMatrixFromLngAndLat(String originLng, String originLat, String destinationLng,
            String destinationLat) {
        StringBuilder url = new StringBuilder();
        url.append(GET_DISTANCE_URL).append("?output=json").append("&origins=").append(originLat).append(",").append(originLng)
                .append("&destinations=").append(destinationLat).append(",").append(destinationLng).append("&ak=").append(KEY);
        String response = HttpTool.httpGet(url.toString());
        return JSON.parseObject(response, RouteMatrix.class);
    }

    public static void main(String[] args) {
        String ori = "福建省厦门市集美区软件园3期B03栋";
        Map<String, String> oriLngLat = BaiduMapApiUtils.getLngAndLat(ori);
        String des = "福建省厦门市集美区软件园3期A03栋";
        Map<String, String> desLngLat = BaiduMapApiUtils.getLngAndLat(des);
        RouteMatrix routeMatrix = BaiduMapApiUtils
                .getRouteMatrixFromLngAndLat(oriLngLat.get("lng"), oriLngLat.get("lat"), desLngLat.get("lng"), desLngLat.get("lat"));
        System.out.println("sss");
    }
}
