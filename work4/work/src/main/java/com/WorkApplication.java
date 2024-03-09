package com;


import org.quartz.SchedulerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 12080
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class WorkApplication {

    public static void main(String[] args) throws SchedulerException {
        SpringApplication.run(WorkApplication.class, args);
    }

}
