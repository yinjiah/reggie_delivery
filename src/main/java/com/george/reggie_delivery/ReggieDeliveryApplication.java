package com.george.reggie_delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggieDeliveryApplication.class, args);
	}

}
