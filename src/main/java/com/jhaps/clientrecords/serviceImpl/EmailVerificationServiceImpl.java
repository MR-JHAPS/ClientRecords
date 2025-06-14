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

import jakarta.transaction.Transactional;
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
	
	

	
	/**
	 * Theoretically This should work.
	 */
	@Override
	@Transactional
	public String generateEmailVerificationUrl(int userId) {
		User user = userService.findUserById(userId);
		log.info("Found User {} by ID: {}", user.getEmail(), userId);
		String verificationCode = generateEmailVerificationCode();
		log.info("This is the verification Code that was received to anotherMethod : {}.",verificationCode);
		if(user.getVerificationCode()!=null && !user.getVerificationCode().isBlank() ) {
			log.info("Setting the user verification Code to null because it is currently not null.");
			user.setVerificationCode(null);
		}
		//saving the random generated verificationCode to the user Database.
		user.setVerificationCode(verificationCode);
		userService.saveUser(user);
		log.info("Verification Code is saved in the user Database.");
		String url = frontEndUrl + emailVerificationPath + "?"+"verification_code="+ verificationCode;
		return url;
	}



	
	@Override
	public String generateEmailVerificationCode() {	
		log.info("Generating the verification Code UUID.");
		String randomCode = UUID.randomUUID().toString();
		log.info("Generated Code is : {}", randomCode);
		return randomCode;
	}
	
		

	

	/**
	 * Validates the Code sent by user through email and from the one saved in the database.
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
	
	
	
	
	
	
}
