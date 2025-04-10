package com.jhaps.clientrecords.serviceImpl.system;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.request.user.UserRegisterRequest;
import com.jhaps.clientrecords.dto.request.user.UserUpdateRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.dto.response.user.UserGeneralResponse;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.RoleNames;
import com.jhaps.clientrecords.exception.system.DuplicateDataException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.client.ClientRepository;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.security.customAuth.PasswordValidator;
import com.jhaps.clientrecords.service.client.ClientService;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.service.system.RoleService;
import com.jhaps.clientrecords.service.system.UserService;
import com.jhaps.clientrecords.util.mapper.ImageMapper;
import com.jhaps.clientrecords.util.mapper.UserMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepo;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder; //Bcrypt PasswordEncoder as configured in securityConfig
	private UserMapper userMapper; 
	private PasswordValidator passwordValidator; // handles password validation
	private ImageService imageService;
	private ClientService clientService;

	
	
	
	public User getCurrentUser(int userId) {
		return findUserById(userId);
	}
	

	/* Saving/registering new user  */
	@Transactional
	@Override
	public void saveNewUser(UserRegisterRequest registrationDto) {
		log.info("Action: Preparing to save new User: {} ", registrationDto.getEmail() );
		if(userRepo.existsByEmail(registrationDto.getEmail())) {
			throw new DuplicateDataException("Unable to save user, User with email : " + registrationDto.getEmail() + " already exists.");
		}
		/* Mapping registrationDto to User-entity */
		User user = userMapper.toUserEntity(registrationDto);
		
		/* Validating the password and confirm password matches.*/
		passwordValidator.validatePasswordMatch(registrationDto.getPassword(), registrationDto.getConfirmPassword()); 
		String encodedPassword = passwordEncoder.encode(registrationDto.getPassword()); // encoding Password
		user.setPassword(encodedPassword);
		
		/* Setting default Role ("user") */
		log.info("Saving new User: {} with default Role: {}" , registrationDto.getEmail(), RoleNames.USER.getRole());
		Role defaultRole = roleService.findRoleByName(RoleNames.USER.getRole()); //error is handled in roleService.
		user.setRoles(new HashSet<>(Set.of(defaultRole)) );	/* The Role in User is of type Set<Role> setting the default value as new HashSet<>(set.of('user')) */
		
		user.setAccountLocked(false);
		user.setAttempts(0);
		// saving user without profile image
		User savedUser = saveUser(user);	
		//we get id of the user after we save and we pass that id to save a default image for that id.
		log.info("Preparing to set new profile picture for user: {}", registrationDto.getEmail());
		Image defaultProfileImage = imageService.saveDefaultProfileImageForGivenUser(savedUser.getId()); //getting default profile image from ImageService.
		savedUser.setProfileImage(defaultProfileImage); //setting default profile image when new account is created.
		//saving with profile image.
		saveUser(savedUser);
		log.info("Action: User with email: {} saved successfully", registrationDto.getEmail());
	}
	


	@Override
	@Transactional
	public void deleteCurrentUser(int userId) {
		log.info("Action: Preparing to delete user: {}", userId);
		User user = findUserById(userId);
		try{
			/* 
			 * Reassigning all the clients of this user to admin before deleting the user.
			 */
			clientService.reassignClientsToAdmin(userId); 			
			/* 
			 * Clears the roles of the given user :
			 */
			user.removeRoles();
			/* Delete the user once  */
			userRepo.delete(user);
			log.info("Action: user Deleted successfully");
		}catch(Exception e) {
			log.error("Error :  Unable to delete the user", e);
		}
	}

	
	
	@Override
	public void updateCurrentUser(int userId, UserUpdateRequest request) {
		User user = findUserById(userId);  		
		log.info("Action: Preparing to update user: {}", user.getEmail());
		
		/* If "current-password" does not match the "encoded-account-password" it will throw exception*/
		passwordValidator.verifyCurrentPassword(request.getCurrentPassword(), user.getPassword());

		/* Checking if user is asking to change a password... if newPassword contains a text proceed to change password.*/
		if(StringUtils.hasText(request.getNewPassword())) {
			/* throws error if newPassword and confirmPassword does not match. */
			passwordValidator.validatePasswordMatch(request.getNewPassword(), request.getConfirmPassword()); 
			String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
			user.setPassword(encodedNewPassword);
		}
		user.setEmail(request.getEmail());
		userRepo.save(user);
		log.info("Action: Update Successful for user: {}, with new data", user.getEmail());
		
	}
	
	

	@Override
	public void updateCurrentUserProfileImage(int userId, UserImageUploadRequest request) {
		User user = findUserById(userId);
		/* If image with this name "is-found" in DB then it returns that image.
		 * If image "is-not-found" in DB it saves the image and returns that image.  
		 */
		Image updatedProfileImage = imageService.updateProfileImage(request.getImageName(), userId);
		user.setProfileImage(updatedProfileImage);
		userRepo.save(user);
	}

	
	/* To remove the custom user profile image and set the default-profile-image. */
	public void removeCurrentUserCustomProfileImage(int userId) {
		User user = findUserById(userId);
		Image defaultImage = imageService.saveDefaultProfileImageForGivenUser(userId);
		user.setProfileImage(defaultImage);
	}
	
	
	
	
	
	/* PRIVATE METHODS:*/
	
	private User findUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(()->
						 new UserNotFoundException("Unable to find the user with Email : " + email));
		log.info("Action: User with email: {} found in the database.", email);
		return user;					
	}
	
	@Override
	public User findUserById(int id) {
		User user = userRepo.findById(id)
				.orElseThrow( ()->new UserNotFoundException("Unable to find the user with ID : " + id) );
		return user;	
	}
	
	
	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}

	
	

}//ends class	



