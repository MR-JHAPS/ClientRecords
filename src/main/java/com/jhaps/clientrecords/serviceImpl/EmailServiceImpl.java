package com.jhaps.clientrecords.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.service.EmailService;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender javaMailSender;
	
	
	
	public void sendEmail(String receiver, String subject, String body) {
		
		try {
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(receiver);
			email.setSubject(subject);
			email.setText(body);
			javaMailSender.send(email);
		} catch (Exception e) {
			log.error("Unable to send Email to : {}", receiver);
		}
	}
	
	
	
	
	
	
	
}
