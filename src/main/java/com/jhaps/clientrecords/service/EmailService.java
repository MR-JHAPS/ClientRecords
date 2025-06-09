package com.jhaps.clientrecords.service;

public interface EmailService {

	
	void sendEmail(String receiver, String subject, String body);
}
