package com.javateer.quartz_example;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.javateer.quartz_example.scheduling.InventoryDataJob;
import com.javateer.quartz_example.scheduling.InventoryStatusDataJob;
import com.javateer.quartz_example.scheduling.PriceDataJob;
import com.javateer.quartz_example.util.ApplicationProperties;

@SpringBootApplication
public class QuartzJobSchedulerExampleApp {

    @Bean
    @ConfigurationProperties("javateer")
    public ApplicationProperties appProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public JobDetail priceJob() {
        return newJob(PriceDataJob.class).withIdentity("Price Job").build();
    }

    @Bean
    public JobDetail inventoryJob() {
        return newJob(InventoryDataJob.class).withIdentity("Inventory Job").build();
    }

    @Bean
    public JobDetail inventoryStatusJob() {
        return newJob(InventoryStatusDataJob.class).withIdentity("Inventory Status Job").build();
    }

    @Bean
    public Trigger priceTrigger() {
        return newTrigger().startNow().withIdentity("Price Trigger").forJob(priceJob())
                .withSchedule(cronSchedule(appProperties().getCronSchedule().getPriceJob())).build();
    }

    @Bean
    public Trigger inventoryTrigger() {
        return newTrigger().startNow().withIdentity("Inventory Trigger")
                .withSchedule(cronSchedule(appProperties().getCronSchedule().getInventoryJob())).forJob(inventoryJob())
                .build();
    }

    @Bean
    public Trigger inventoryStatusTrigger() {
        return newTrigger().startNow().withIdentity("Inventory Status Trigger").forJob(inventoryStatusJob())
                .withSchedule(cronSchedule(appProperties().getCronSchedule().getInventoryStatusJob())).build();
    }

    @Bean()
    public Scheduler scheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(priceJob(), priceTrigger());
        scheduler.scheduleJob(inventoryJob(), inventoryTrigger());
        scheduler.scheduleJob(inventoryStatusJob(), inventoryStatusTrigger());

        return scheduler;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuartzJobSchedulerExampleApp.class, args);
    }
}
