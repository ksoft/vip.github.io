---
layout: post
title: spring ThreadPoolTaskExecutor
category: 技术
tags: spring ThreadPoolTaskExecutor
keywords: spring ThreadPoolTaskExecutor
---

## 配置taskExecutor

```markdown
<bean id="taskExecutor" class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor">
		<property name="corePoolSize" value="${task.core_pool_size}" />
		<property name="maxPoolSize" value="${task.max_pool_size}" />
		<property name="queueCapacity" value="${task.queue_capacity}" />
		<property name="keepAliveSeconds" value="${task.keep_alive_seconds}" />
	</bean>
```

## 使用(把线程扔进去，线程可以是继承Thread类或实现Runnable接口)
```markdown

taskExecutor.execute(new Runnable() {
			public void run() {
				//to do something here
			}
		});
```