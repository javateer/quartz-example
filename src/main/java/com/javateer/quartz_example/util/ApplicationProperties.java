package com.javateer.quartz_example.util;

public class ApplicationProperties {

    private final CronSchedule cronSchedule = new CronSchedule();

    public CronSchedule getCronSchedule() {
        return cronSchedule;
    }

    public static class CronSchedule {

        private String priceJob;

        private String inventoryJob;

        private String inventoryStatusJob;

        public String getPriceJob() {
            return priceJob;
        }

        public void setPriceJob(String priceJob) {
            this.priceJob = priceJob;
        }

        public String getInventoryJob() {
            return inventoryJob;
        }

        public void setInventoryJob(String inventoryJob) {
            this.inventoryJob = inventoryJob;
        }

        public String getInventoryStatusJob() {
            return inventoryStatusJob;
        }

        public void setInventoryStatusJob(String inventoryStatusJob) {
            this.inventoryStatusJob = inventoryStatusJob;
        }
    }
}
