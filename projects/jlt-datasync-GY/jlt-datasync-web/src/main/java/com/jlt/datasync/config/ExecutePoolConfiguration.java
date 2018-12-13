package com.jlt.datasync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Billy.Zhang
 * @date 2018/9/10
 */
@Configuration
@ConfigurationProperties(prefix="threadpool")
public class ExecutePoolConfiguration {

    @Value("${threadpool.core-pool-size}")
    private int corePoolSize;

    @Value("${threadpool.max-pool-size}")
    private int maxPoolSize;

    @Value("${threadpool.queue-capacity}")
    private int queueCapacity;

    @Value("${threadpool.keep-alive-seconds}")
    private int keepAliveSeconds;


    @Bean(name="threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setCorePoolSize(corePoolSize);//核心线程池数
        executor.setMaxPoolSize(maxPoolSize); // 最大线程
        executor.setQueueCapacity(queueCapacity);//队列容量
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //队列满，线程被拒绝执行策略
        return executor;
    }

    /**
     * 自定义异步线程池
     *
     * @return
     */
    @Bean(name="asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Async-Executor");
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setCorePoolSize(corePoolSize);//核心线程池数
        executor.setMaxPoolSize(maxPoolSize); // 最大线程
        executor.setQueueCapacity(queueCapacity);//队列容量

        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // .....
            }
        });
        // 使用预定义的异常处理类
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }
}
