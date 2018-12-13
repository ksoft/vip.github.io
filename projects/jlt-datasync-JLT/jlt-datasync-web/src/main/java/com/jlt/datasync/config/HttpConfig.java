package com.jlt.datasync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Billy.Zhang
 * @date 2018/11/29
 */
@Configuration
public class HttpConfig {
    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000);
        httpRequestFactory.setConnectTimeout(30000);
        httpRequestFactory.setReadTimeout(300000);

        return new RestTemplate(httpRequestFactory);
    }
}
