package com.javateer.quartz_example.scheduling;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

public class InventoryDataJob implements Job {

    @Inject
    ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        if (applicationContext == null) {
            throw new RuntimeException("Failed to inject dependencies.");
        }
        else {
            System.out.println("InventoryDataJob fired.");
        }
    }

}
