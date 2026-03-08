package com.yagowill.asset_automation_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AssetAutomationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetAutomationApiApplication.class, args);
	}

}
