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

	
	

//-------------------------------THESE ARE CRUD-----------------------------------------------------------------------------------------	
	
	@Override
	public User findUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(()->
						 new UserNotFoundException("Unable to find the user with Email : " + email));
		log.info("Action: User with email: {} found in the database.", email);
		return user;					
	}
	
	/* Return type is "User" | It is used for internal business logic. */
	@Override
	public User findUserById(int id) {
		User user = userRepo.findById(id)
				.orElseThrow( ()->new UserNotFoundException("Unable to find the user with ID : " + id) );
		return user;	
	}
	
	
	/* No Logic just saving the user that is ready. */
	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}
	
	
	
	/* Saving/registering new user with logic. */
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
	
	
	
	
	
	
	
	//Deleting the User along with their existing images from imageRepo.
	@Transactional
	@Override
	public void deleteUserByEmail(String userEmail)  { 
		log.info("Action: Preparing to delete user: {}", userEmail);
		User user = findUserByEmail(userEmail);
		try{
			clientService.reassignClientToAdmin(userEmail); // this will reassign all the clients to admin before deleting the user.
		
			if(user.getProfileImage() != null) {
				user.setProfileImage(null); // removing the profile image from the user.
				userRepo.save(user);
			}
		
			//Deletes all the images of the given user.
			imageService.deleteAllImagesOfGivenUser(user.getId());
			
			// Clears the roles of the given user.
			user.getRoles().clear(); //removing the roles of this user.
			userRepo.save(user); // saving the user without roles.
			
			//Delete the user.
			userRepo.delete(user);
			log.info("Action: user Deleted successfully");
		}catch(Exception e) {
			log.error("Error :  Unable to delete the user", e);
		}
		
	}//ends method.
	
	
	
	
	
	
	
	/*
	 * @param : email is used to find the user and udpate their Data.
	 * @UserUpdate_dto contains, newPassword, confirmPassword and currentPassword and other details.
	 * 
	 * @userUpdate.getCurrentPassword : required to update the user for verifying if the account belongs to the user.
	 * */
	
	@Transactional
	@Override
	public void updateUserByEmail(String email, UserUpdateRequest userUpdateRequest) {
		log.info("Action: Preparing to update user: {}", email);
		User user = findUserByEmail(email);  		
		/* If "current-password" does not match the "encoded-account-password" it will throw exception*/
		passwordValidator.verifyCurrentPassword(userUpdateRequest.getCurrentPassword(), user.getPassword());
		
		/* Checking if user is asking to change a password... if newPassword contains a text proceed to change password.*/
		if(StringUtils.hasText(userUpdateRequest.getNewPassword())) {
			/* throws error if newPassword and confirmPassword does not match. */
			passwordValidator.validatePasswordMatch(userUpdateRequest.getNewPassword(), userUpdateRequest.getConfirmPassword()); 
			String encodedNewPassword = passwordEncoder.encode(userUpdateRequest.getNewPassword());
			user.setPassword(encodedNewPassword);
		}
		user.setEmail(userUpdateRequest.getEmail());
		userRepo.save(user);
		log.info("Action: Update Successful for user: {}", email);
	}

	
	/* This class is to update the userProfileImage */
	@Transactional
	public void updateUserProfileImage(String email, UserImageUploadRequest userImageUploadRequest) {
		User user = findUserByEmail(email);
		/*	Converting UserUpdateImage to ImageRequest because : 
		 * 					imageService.saveImage(String, ImageRequest).
		 * @param of saveImage in imageService is of type ImageRequest.
		 * */
		log.info("mapping Image: {}, UserImageUploadRequest to ImageRequest", userImageUploadRequest.getImageName());
		ImageRequest imageRequest = userMapper.toImageRequestFromUserUpdateImage(userImageUploadRequest);
		/* If the image exists in the database by given imageName and userEmail we set that image as user profile. */
		if(imageService.doesImageExistsByImageNameAndUserEmail(imageRequest.getImageName(), user.getEmail())) {
			/* we fetch that image and save it as the current userProfile-picture.*/
			Image foundImage = imageService.findByImageNameAndUserEmail(imageRequest.getImageName(), email);			
			user.setProfileImage(foundImage);
			userRepo.save(user);
			return;
		}
		/* If requested Image is not found in the database then we save the image to the database. */
		Image savedImage = imageService.saveImage(email, imageRequest); 
		user.setProfileImage(savedImage);
		userRepo.save(user);
		
	}

	


}//ends class	


	
	
//}//ends class
