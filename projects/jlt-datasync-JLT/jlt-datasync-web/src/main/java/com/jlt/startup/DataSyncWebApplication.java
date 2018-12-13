package com.jlt.startup;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ImportResource({"classpath:popwin.xml", "classpath:datasync/context-*.xml"})
@SpringBootApplication(scanBasePackages = "com.jlt")
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
public class DataSyncWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSyncWebApplication.class, args);
    }
}
