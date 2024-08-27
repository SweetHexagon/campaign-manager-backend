package com.task_project.campaign_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class CampaignManagerApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CampaignManagerApplication.class, args);
	}


}
