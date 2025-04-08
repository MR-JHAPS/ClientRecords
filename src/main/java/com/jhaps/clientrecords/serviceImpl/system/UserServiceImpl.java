package com.jhaps.clientrecords.serviceImpl.system;

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
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.security.customAuth.PasswordValidator;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.service.system.RoleService;
import com.jhaps.clientrecords.service.system.UserService;
import com.jhaps.clientrecords.util.mapper.ImageMapper;
import com.jhaps.clientrecords.util.mapper.UserMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	@Autowired
	private ImageMapper imageMapper;
	
	private UserRepository userRepo;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder; //Bcrypt PasswordEncoder as configured in securityConfig
	private UserMapper userMapper; 
	private PasswordValidator passwordValidator; // handles password validation
	private ImageService imageService;

	
	public UserServiceImpl(UserRepository userRepo, RoleService roleService, 
							PasswordEncoder passwordEncoder,
							UserMapper userMapper, ImageService imageService,
							PasswordValidator passwordValidator) {
		this.userRepo = userRepo;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
		this.passwordValidator = passwordValidator;
		this.imageService = imageService;
	}

//-------------------------------THESE ARE CRUD-----------------------------------------------------------------------------------------	
	
	@Override
	public User findUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(()->
						 new UserNotFoundException("Unable to find the user with Email : " + email));
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
	public void saveUser(User user) {
		userRepo.save(user);
	}
	
	
	/* Returns "UserGeneralDto" this is for User-Profile.*/
	@Override			
	public UserGeneralResponse findUserDtoByEmail(String email) {
		User user = findUserByEmail(email);
		return userMapper.toUserGeneralResponse(user);	
	}
	
	
	/* Saving/registering new user with logic. */
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
		log.info("Saving new User: {} with default Role: {}" , registrationDto.getEmail(), RoleNames.USER.getRole());
		Role defaultRole = roleService.findRoleByName(RoleNames.USER.getRole()); //error is handled in roleService.
		user.setRoles(Set.of(defaultRole));	/* The Role in User is of type Set<Role> setting the default value as set.of('user') */
		user.setAccountLocked(false);
		user.setAttempts(0);
		
		Image defaultProfileImage = imageService.getDefaultProfileImage(); //getting default profile image from ImageService.
		user.setProfileImage(defaultProfileImage); //setting default profile image when new account is created.
		saveUser(user);	
		log.info("Action: User with email: {} saved successfully", registrationDto.getEmail());
	}
	
	
	
	//Deleting the User along with their existing images from imageRepo.
	@Transactional
	@Override
	public void deleteUserByEmail(String email)  { 
		log.info("Action: Preparing to delete user: {}", email);
		User user = findUserByEmail(email);
		imageService.deleteImagesByUserEmail(email); //deletes the images of the user before deleting the account.
		userRepo.delete(user);
		log.info("Action: user Deleted successfully");
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
		 * 			imageService.saveImage(String, ImageRequest)
		 * @param of saveImage in imageService is of type ImageRequest.
		 * */
		log.info("mapping Image: {}, UserImageUploadRequest to ImageRequest", userImageUploadRequest.getImageName());
		ImageRequest imageRequest = userMapper.toImageRequestFromUserUpdateImage(userImageUploadRequest);
		
		/* Checking if the userImageUploadRequest contains the defaultImage-name */
		boolean isDefaultImage = imageService.isDefaultImage(imageRequest);
		
		
		if(isDefaultImage) {
			/* This is for testing purpose only*/
			log.info("Action: Image sent through request is a default image");
			Image defaultImage = imageService.getDefaultProfileImage();
			
			user.setProfileImage(defaultImage);
			userRepo.save(user);
			return;
		}
			/* This is for the production.
			 * saving the image in the userRepository through imageService.
			 * After passing ImageRequest we now get ImageResponse from user Response
			 */
			ImageResponse imageResponse = imageService.saveImage(email, imageRequest); 
			Image image = imageService.getImageById(imageResponse.getId()); // getting image from db using imageResponse.getId().
			user.setProfileImage(image);
			userRepo.save(user);
		
	}

	


}//ends class	
	
//-------------------------------THESE ARE PRIVATE Methods----------------------------------------------------------------------------------------------------------
	
		
//		//Checking if the role exists in the RoleNames.enum.(Single Role)
//			private boolean isRoleValid(String roleName) {
//				boolean isFound = false;
//				for(RoleNames role : RoleNames.values()) {
//					if(roleName.equals(role.getRole())) {
//						 isFound=true;
//						 break;
//					}
//				}//ends for-Loop.
//				return isFound;
//			}

//			//this is for the validation of Set<String> roles. (Multiple Roles at once)
//			private boolean isRoleValid(Set<String> roleNames) {
//				Set<String> validRoles = Arrays.stream(RoleNames.values())
//										.map(RoleNames::getRole)
//										.collect(Collectors.toSet());
//				
//				return roleNames.stream()
//								.allMatch(r->validRoles.contains(r));
//			}
			



	
	
//}//ends class
