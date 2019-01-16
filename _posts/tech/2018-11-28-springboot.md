---
layout: post
title: springboot
category: 技术
tags: springboot
keywords: springboot
---
## SpringBoot

Controller默认单例

## 配置RedisConfig
```markdown
/**
 * @author zhangjianbo
 * @date 2018/10/12
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
        return cacheManager;
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        return redisTemplate;
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }
}
```
## 配置线程池
```markdown
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
```
## 配置拦截器
1、启动类添加注解：@EnableAsync  
2、配置Aspect类  
```markdown
/**
 * @author Billy.Zhang
 * @date 2018/9/26
 */
@Aspect
@Component
public class JobLogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 定义pointcut，可拦截注解或定义包/方法
     */
    @Pointcut("@annotation(com.xxx.common.annotation.JobLog)")
    public void jobLog() {
    }

    @Before("jobLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        try {
            XxxJobDetail xxxJobDetail = (XxxJobDetail) joinPoint.getArgs()[0];
            String msg = "";
            if (JobType.READ.getKey().equals(xxxJobDetail.getJobType())) {
                msg = "【" + xxxJobDetail.getTableName() + "】,开始抽取数据 ";
            } else {
                msg = "【" + xxxJobDetail.getTableName() + "】,开始写入数据 ";
            }
            logger.info(msg);
            //xxxJobDetailLogBiz.insertLog(xxxJobDetail, msg, new Date());
        } catch (Exception e) {
            logger.error("日志处理异常，不影响业务逻辑！错误信息：" + e.getMessage());
        }
    }

    @AfterReturning(returning = "ret", pointcut = "@annotation(jobLog)")
    public void doAfterReturning(JoinPoint joinPoint, Object ret, JobLog jobLog) throws Throwable {
        try {
            JobType jobType = jobLog.jobTye();
            XxxJobDetail xxxJobDetail = (XxxJobDetail) joinPoint.getArgs()[0];
            String msg = "";
            if (JobType.READ.equals(jobType)) {
                DataTransferDto dataTransferDto = (DataTransferDto) joinPoint.getArgs()[1];
                int count = 0;
                List<Map<String, Object>> dataList;
                if (DataSchema.SAAS.getKey().equals(xxxJobDetail.getSchemaName())) {
                    dataList = dataTransferDto.getSaasDataMap().get(xxxJobDetail.getTableName());
                } else if (DataSchema.UPM.getKey().equals(xxxJobDetail.getSchemaName())) {
                    dataList = dataTransferDto.getUpmDataMap().get(xxxJobDetail.getTableName());
                } else {
                    dataList = dataTransferDto.getPmsDataMap().get(xxxJobDetail.getTableName());
                }
                if (dataList != null) {
                    count = dataList.size();
                }
                msg = "【"+xxxJobDetail.getTableName()+"】，抽取数据成功，数量：" + count;
            } else {
                msg += "【" + xxxJobDetail.getTableName() + "】，写入数据成功";
            }
            logger.info(msg);
            //xxxJobDetailLogBiz.insertLog(xxxJobDetail, msg, new Date());
        } catch (Exception e) {
            logger.error("日志处理异常，不影响业务逻辑！错误信息：" + e.getMessage());
        }
    }
}
```
