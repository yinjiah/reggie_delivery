package com.george.reggie_delivery;

import com.george.reggie_delivery.entity.DishFlavor;
import com.george.reggie_delivery.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@SpringBootTest
@Slf4j
class ReggieDeliveryApplicationTests {

	@Autowired
	Environment environment;
	@Value("${server.port}")
	Integer port;
	@Autowired
	private DishFlavorService dishFlavorService;

	@Autowired
	private MailSender mailSender;
	@Value("${spring.mail.username}")
	private String from;

	@Test
	void getPort(){
		log.info("----{}",port);
	}
	@Test
	void contextLoads() {

		DishFlavor dish = dishFlavorService.getById(1413389684020682760L);
		System.out.println(dish);
	}

	@Test
	void testMailSend(){
		//创建一个简单文本邮件的对象
		SimpleMailMessage message = new SimpleMailMessage();
		//赋予相应的内容
		message.setTo("18895362552@139.com");
		message.setSubject("测试短信");
		message.setText("这是邮件的内容");
		message.setFrom(from);
		//将邮件对象赋予邮件发送器
		mailSender.send(message);
	}
}
