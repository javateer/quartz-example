package com.javateer.quartz_example;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.javateer.quartz_example.scheduling.AutowiringSpringBeanJobFactory;
import com.javateer.quartz_example.scheduling.InventoryDataJob;
import com.javateer.quartz_example.scheduling.InventoryStatusDataJob;
import com.javateer.quartz_example.scheduling.PriceDataJob;
import com.javateer.quartz_example.util.ApplicationProperties;

@SpringBootApplication
public class QuartzJobSchedulerExampleApp {

    @Inject
    private ApplicationContext applicationContext;

    @Bean
    @ConfigurationProperties("javateer")
    public ApplicationProperties appProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public JobDetail priceJob() {
        return newJob(PriceDataJob.class)
                .withIdentity("Price Job")
                .storeDurably(true)
                .build();
    }

    @Bean
    public JobDetail inventoryJob() {
        return newJob(InventoryDataJob.class)
                .withIdentity("Inventory Job")
                .storeDurably(true)
                .build();
    }

    @Bean
    public JobDetail inventoryStatusJob() {
        return newJob(InventoryStatusDataJob.class)
                .withIdentity("Inventory Status Job")
                .storeDurably(true)
                .build();
    }

    @Bean
    public Trigger priceTrigger() {
        return newTrigger()
                .startNow()
                .withIdentity("Price Trigger")
                .forJob(priceJob())
                .withSchedule(cronSchedule(appProperties().getCronSchedule().getPriceJob()))
                .build();
    }

    @Bean
    public Trigger inventoryTrigger() {
        return newTrigger()
                .startNow()
                .withIdentity("Inventory Trigger")
                .forJob(inventoryJob())
                .withSchedule(cronSchedule(appProperties().getCronSchedule().getInventoryJob()))
                .build();
    }

    @Bean
    public Trigger inventoryStatusTrigger() {
        return newTrigger()
                .startNow()
                .withIdentity("Inventory Status Trigger")
                .forJob(inventoryStatusJob())
                .withSchedule(cronSchedule(appProperties().getCronSchedule().getInventoryStatusJob()))
                .build();
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(priceJob(), inventoryJob(), inventoryStatusJob());
        schedulerFactory.setTriggers(priceTrigger(), inventoryTrigger(), inventoryStatusTrigger());
        return schedulerFactory;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuartzJobSchedulerExampleApp.class, args);
    }
}
