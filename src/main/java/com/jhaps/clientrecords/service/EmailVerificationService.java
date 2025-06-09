package com.jhaps.clientrecords.service;

public interface EmailVerificationService {

	
	String generateEmailVerificationCode();
	
	String generateEmailVerificationUrl(int userId);
	
	void sendVerificationEmail(int userId);
	
	void verifyUserEmailAddress(int userId, String verificationCode );
	
	
	
	
	
	
}
