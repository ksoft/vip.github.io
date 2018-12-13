package com.jlt.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jlt.common.constant.APIConstant;
import com.jlt.common.constant.Globals;
import com.jlt.datasync.dto.DataTransferDto;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class HttpClientForSinopharmFollow {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientForSinopharmFollow.class);

    private static RestTemplate restTemplate;
    @Autowired
    private RestTemplate restTemplateAutoWire;

    @PostConstruct
    private void init() {
        restTemplate = restTemplateAutoWire;
    }

    private HttpClientForSinopharmFollow() {

    }

    /**
     * 从连接池中取连接的超时时间,默认时间单位为：毫秒秒
     */
    private static final int CON_RST_TIME_OUT = 10000;
    /**
     * 通过网络与服务器建立连接的超时时间,默认时间单位为：毫秒
     */
    private static final int CON_TIME_OUT = 15000;
    /**
     * socket读数据的超时时间,默认时间单位为：毫秒
     */
    private static final int SOCKET_TIME_OUT = 300000;

    /**
     * @param
     * @return
     * @throws Exception
     * @Description:执行请求
     */
    @SuppressWarnings("unchecked")
    public static ApiResultMessage excute(DataTransferDto dataTransferDto, String ediUrl) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CON_RST_TIME_OUT).setConnectTimeout(CON_TIME_OUT)
                .setSocketTimeout(SOCKET_TIME_OUT).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        ApiResultMessage resultMessage = new ApiResultMessage(Globals.ERROR);
        try {
            HttpPost method = new HttpPost(ediUrl);
            String requestJson =
                    JSONObject.toJSONStringWithDateFormat(dataTransferDto, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
            StringEntity entity = new StringEntity(requestJson, APIConstant.UTF_8_LETTER);// 解决中文乱码问题
            entity.setContentEncoding(APIConstant.UTF_8);
            entity.setContentType(APIConstant.APPLICATION_JSON);
            method.setEntity(entity);
            CloseableHttpResponse result = client.execute(method);
            HttpEntity resData = result.getEntity();
            String response = EntityUtils.toString(resData, APIConstant.UTF_8);
            JSONObject json = JSONObject.parseObject(response);
            resultMessage = JSONObject.toJavaObject(json, ApiResultMessage.class);
        } catch (Exception e) {
            logger.error("edi 请求失败：" + e.getMessage());
            resultMessage.setMessage(e.getMessage());
            throw e;
        } finally {
            client.close();
        }
        return resultMessage;
    }

    public static ApiResultMessage excuteWithoutEdi(DataTransferDto dataTransferDto, String recvUrl) throws Exception {
        Long startTime = System.currentTimeMillis();
        ApiResultMessage resultMessage;
        try {
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            String jsonString =
                    JSONObject.toJSONStringWithDateFormat(dataTransferDto, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
            org.springframework.http.HttpEntity<String> formEntity = new org.springframework.http.HttpEntity<>(jsonString, headers);
            resultMessage = restTemplate.postForObject(recvUrl, formEntity, ApiResultMessage.class);
        } catch (Exception e) {
            logger.error("recvUrl 请求失败：" + e.getMessage());
            throw e;
        }
        Long endTime = System.currentTimeMillis();
        logger.info("http execute time:" + (endTime - startTime) / 1000 + " 秒");
        return resultMessage;
    }

}
