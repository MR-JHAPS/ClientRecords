package com.jhaps.clientrecords.serviceImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.EmailVerificationException;
import com.jhaps.clientrecords.service.EmailService;
import com.jhaps.clientrecords.service.EmailVerificationService;
import com.jhaps.clientrecords.service.system.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${frontend_url}")
	private String frontEndUrl ; 
	
	// This is like "/verificationPage"
	@Value("${email_verification_path}")
	private String emailVerificationPath;
	
	
	
	@Override
	public String generateEmailVerificationCode() {	
		String randomCode = UUID.randomUUID().toString();
		return randomCode;
	}
	
	
	

	/**
	 * @returns : Url that is sent to email. That 
	 *
	 */
	@Override
	public void verifyUserEmailAddress(int userId,  String verificationCode) {
		User user = userService.findUserById(userId);
		String verificationCodeDb = user.getVerificationCode();
		if(!verificationCodeDb.equals(verificationCode)){
			log.error("Error! Submitted Verification code mismatches or has Expired");
			throw new EmailVerificationException("Wrong verification Code.");
		}		
		user.setEmailVerified(true);
		user.setVerificationCode(null);
		userService.saveUser(user);
	}
	
	
	
	
	/**
	 * Theoretically This should work.
	 */
	@Override
	public String generateEmailVerificationUrl(int userId) {
		User user = userService.findUserById(userId);
		String verificationCode = generateEmailVerificationCode();
		if(!user.getVerificationCode().isBlank() || user.getVerificationCode()!=null) {
			user.setVerificationCode(null);
		}
		//saving the random generated verificationCode to the user Database.
		user.setVerificationCode(verificationCode);
		userService.saveUser(user);
		String url = frontEndUrl + emailVerificationPath + "?"+"verification_code="+ verificationCode;
		return url;
	}




	@Override
	public void sendVerificationEmail(int userId) {
		String url = generateEmailVerificationUrl(userId);
		log.info("Preparing to send the verification Email to the User.");
		User user = userService.findUserById(userId);
		String userEmail = user.getEmail();
		String subject ="ClientRecords User Email Verification.";
		String body = """
		        Dear %s,

		        Thank you for registering with ClientRecords.

		        Please click the link below to verify your email address:

		        %s

		        If you did not request this, please ignore this email.

		        Best regards,
		        ClientRecords Team
		        """.formatted(user.getEmail(), url);


		emailService.sendEmail(userEmail, subject, body);		
	}
	
	
	
	
	
	
	
	
}
