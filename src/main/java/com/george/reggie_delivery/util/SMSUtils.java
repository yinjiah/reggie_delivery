package com.george.reggie_delivery.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * 短信发送工具类
 */
@Component
public class SMSUtils {

	@Value("${spring.mail.username}")
	private String from;

	@Autowired
	private MailSender mailSender;

	private SimpleMailMessage message = new SimpleMailMessage();


	public void sendMail(String to,Integer code){
		//赋予相应的内容
		message.setTo(to);
		message.setSubject("验证码接受");
		message.setText("您的验证码为：" + code + "，请您妥善保管！" );
		message.setFrom(from);
		//将邮件对象赋予邮件发送器
		mailSender.send(message);
	}

}
