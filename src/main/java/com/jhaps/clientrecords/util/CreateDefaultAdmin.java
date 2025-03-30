package com.jhaps.clientrecords.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.RoleNames;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.service.system.RoleService;

import jakarta.transaction.Transactional;

/*
 * This class is supposed to create a default "admin user" upon the first execution of the application
 * with the roles["admin", "user"] that is necessary to manipulate any data.
 * 
 * */

@Component
public class CreateDefaultAdmin implements CommandLineRunner {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;	
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		

		/*					 Roles :				 */
		Set<String> roles = Set.of(RoleNames.ADMIN.getRole(), RoleNames.USER.getRole()); /* set of roles we want to set in User*/ 
		Set<Role> rolesOnDb = roleService.findRoleByNames(roles); /* Validating the roles from Database/repository. */
		
		String adminEmail = "admin@gmail.com"; /* Default email for Admin*/
		
		boolean emailExistsInDb = userRepo.existsByEmail(adminEmail);  /* Checking if the email Exists in the Database. */
		
		/* Only Proceed if the email does not exist in Database/repository */		
		if(!emailExistsInDb) {
			User user = new User();
			user.setEmail(adminEmail); /* Setting admin email*/ 
			
			String password = "1111";	/* Plain password without encoding*/ 
			String encodedPassword = passwordEncoder.encode(password); /* Encoding the plain password*/ 
			user.setPassword(encodedPassword);  /* Setting admin encoded password in DB*/ 
			
			user.setRoles(rolesOnDb);
			user.setAccountLocked(false);
			user.setAttempts(0);
			userRepo.save(user); /* saving the Admin information */
		}
		
		
		
		
		
		
		
	}//ends implemented method

	
}// ends class
