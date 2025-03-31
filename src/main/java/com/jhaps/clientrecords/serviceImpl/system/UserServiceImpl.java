package com.jhaps.clientrecords.serviceImpl.system;

import java.util.Set;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jhaps.clientrecords.dto.request.user.UserRegister;
import com.jhaps.clientrecords.dto.request.user.UserUpdate;
import com.jhaps.clientrecords.dto.response.UserDto;
import com.jhaps.clientrecords.dto.response.user.UserAdmin;
import com.jhaps.clientrecords.dto.response.user.UserGeneralDto;
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
import com.jhaps.clientrecords.util.Mapper;
import com.jhaps.clientrecords.util.SecurityUtils;
import com.jhaps.clientrecords.util.UserMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

	private UserRepository userRepo;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder; //Bcrypt PasswordEncoder as configured in securityConfig
	private Mapper mapper;
	private UserMapper userMapper; //Maps user
	private final SecurityUtils securityUtils;  // it's method extracts userDetails from SecurityContextHolder
	private PasswordValidator passwordValidator; // handles password validation
	private ImageService imageService;
	
	public UserServiceImpl(UserRepository userRepo, RoleService roleService, 
							Mapper mapper, PasswordEncoder passwordEncoder,
							UserMapper userMapper, SecurityUtils securityUtils,
							PasswordValidator passwordValidator) {
		this.userRepo = userRepo;
		this.roleService = roleService;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
		this.securityUtils = securityUtils;
		this.passwordValidator = passwordValidator;
	}

//-------------------------------THESE ARE CRUD----------------------------------------------------------------------------------------------------------	
//	@Override
//	public Page<UserDto> findAllUsers(Pageable pageable) {
//		Page<User> userList = userRepo.findAll(pageable);
//		if(userList.getContent().isEmpty()) {
//			throw new UserNotFoundException("No users Found in the Database");
//		}
//		log.info("Finding All Users is Executed Successfully. and fetched :{} clients", userList.getNumberOfElements());
//		return userList.map(mapper::toUserDto);
//	}
	
	
	/* Returns "UserGeneralDto" this is for User-dashboard.*/
	@Override			
	public UserGeneralDto findUserDtoByEmail(String email) {
		User user = findUserByEmail(email);
		return userMapper.toUserGeneralDto(user);	
	}
	
	
//	/* Returns "UserAdminDto" this contains userRoles to view in admin-Dashboard */
//	public UserAdminDto findUserWithRolesById(int id) {
//		User user = userRepo.findById(id).orElseThrow( ()->
//			new UserNotFoundException("Unable to find the user with ID : " + id) );
//		return userMapper.toUserAdminDto(user);
//	}
	
	
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
	
	
	/* Saving/registering new user with logic. */
	@Override
	public void saveNewUser(UserRegister registrationDto) {
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
		/* The Role in User is of type Set<Role> setting the default value as set.of('user') */
		user.setRoles(Set.of(defaultRole));	
		user.setAccountLocked(false);
		user.setAttempts(0);
		saveUser(user);	
		log.info("Action: User with email: {} saved successfully", registrationDto.getEmail());
	}
	
	
	
	/* --'SecurityUtils'-- contains the all the login Info of the active user. */
	@Transactional
	@Override
	public void deleteUserByEmail(String email)  { 
			log.info("Action: Preparing to delete user: {}", email);
		Set<String> roles = securityUtils.getAuthoritiesFromCustomUserDetails(); //this is the private method written above.
			log.info("Current user roles are : {}", roles);
		User user = findUserByEmail(email);
		imageService.deleteImagesByUserEmail(email); //deletes the images of the user before deleting the account.
		userRepo.delete(user);
		log.info("Action: user Deleted successfully");
	}//ends method.
	
	
	
	//To update the user you also need to provide the current password for verification purpose.
	@Transactional
	@Override
	public void updateUserByEmail(String email, UserUpdate userUpdate) {
		log.info("Action: Preparing to update user: {}", email);
		User user = findUserByEmail(email);  		
		/* If "current-password" does not match the "encoded-account-password" it will throw exception*/
		passwordValidator.verifyCurrentPassword(userUpdate.getCurrentPassword(), user.getPassword());
		
		/* Checking if user is asking to change a password*/
		if(StringUtils.hasText(userUpdate.getNewPassword())) {
			/* throws error if newPassword and confirmPassword does not match. */
			passwordValidator.validatePasswordMatch(userUpdate.getNewPassword(), userUpdate.getConfirmPassword()); 
			String encodedNewPassword = passwordEncoder.encode(userUpdate.getNewPassword());
			user.setPassword(encodedNewPassword);
		}
		user.setEmail(userUpdate.getEmail());
		userRepo.save(user);
		log.info("Action: Update Successful for user: {}", email);
	}

	
	
	//THIS IS ONLY PERMITTED TO THE ADMIN.
//	@Override
//	public void updateUserRoleById(int id, RoleDto roleDto) {
//		User user = findUserById(id); //this is the private method written above.
//		Set<String> activeUserRoles = securityUtils.getAuthoritiesFromCustomUserDetails(); //getting the roles of the logged in user.
//		
//		if(!activeUserRoles.contains(RoleNames.ADMIN.getRole())) {
//			throw new AccessDeniedException("You do not have authorization to update the role");
//		}
//		Set<String> roleNamesFromDto = roleDto.getRoleNames(); // this contains the Set<String> of roles from front-end.
//		log.info("updating role of User with id: {} with roles :{}", id, roleDto.getRoleNames());
//		//checking if the roles exists in the roleRepository.
//		Set<Role> roles = roleService.findRoleByNames(roleNamesFromDto);
//		user.getRoles().clear(); // clearing previous roles
//		user.setRoles(roles); // setting new roles.
//		userRepo.save(user);
//		log.info("User Role of id :{} Updated successfully",id);
//	}

	
	
	
	//Email field is unique so instead of list it returns only single User.
//	@Override
//	public UserDto findUserDtoByEmail(String email) {
//		User user = userRepo.findByEmail(email).orElseThrow(()->
//						 new UserNotFoundException("Unable to find the user with Email : " + email));
//		return mapper.toUserDto(user);					
//	}
	
	@Override
	public User findUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(()->
						 new UserNotFoundException("Unable to find the user with Email : " + email));
		return user;					
	}


	
//	//Getting the list of user by their role|Authority.
//	@Transactional
//	@Override
//	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable) {
//		//checking if the argument roleName is valid before proceeding to Database.
//		boolean isRoleFound = roleService.isRoleValid(roleName); //using isRoleValid() method written above to see if role is valid in RoleNames.enum
//		if(!isRoleFound) {
//			throw new RoleNotFoundException("Unable to find Role with name : " + roleName);	
//		}
//		Page<User> userList = userRepo.findByRoles_Name(roleName, pageable);
//		if(userList.getContent().isEmpty()) {
//			throw new UserNotFoundException("No users found in Database with given Role");
//		}
//		return userList.map(mapper::toUserDto);
//	}


	
	
	
	
/*---------------------------------------- private methods--------------------------------------------------------------------------------*/	
//	
//	/*
//	 * @param password is your entered password
//	 * @param confirmPassword is to confirm your password
//	 * 		password and 
//	 * */
//	private void validatePasswordMatch(String password, String confirmPassword) {
//		if(password==null || confirmPassword==null) {
//			throw new IllegalArgumentException("Password Field Cannot be null");
//		}
//		
//		if(!password.equals(confirmPassword)) {
//			throw new IllegalArgumentException(" Password and Confirm-Password mismatch" );
//		}
//	}
//	
//	
//	/*
//	 * This methods checks if the raw password given by user matches the encoded password saved in Database.
//	 * @param : currentRawPassword is raw password given by user for verification.
//	 * @param : passwordInDb is the encoded password saved in userDatabase.
//	 * 
//	 * */
//	private void verifyCurrentPassword(String currentRawPassword, String passwordInDb) {
//		
//		if (currentRawPassword == null || passwordInDb == null) {
//	        throw new BadCredentialsException("Password verification failed");
//	    }
//		
//		if(!passwordEncoder.matches(currentRawPassword, passwordInDb)) {
//			throw new BadCredentialsException(" Your current Password Does not matches your account password");
//		}
//	}
	

//	@Override
//	public void updateAdmin(String currentUserEmail, AdminUpdate adminUpdate) {
//		User user = securityUtils.getCurrentUser(); //getting current User from securityContextHolder
//		
//		/* If "current-password" does not match the "encoded-account-password" it will throw exception*/
//		verifyCurrentPassword(adminUpdate.getCurrentPassword(), user.getPassword());
//		
//		/* Checking if user is asking to change a password*/
//		if(StringUtils.hasText(adminUpdate.getNewPassword())) {
//			/* throws error if newPassword and confirmPassword does not match. */
//			validatePasswordMatch(adminUpdate.getNewPassword(), adminUpdate.getConfirmPassword()); 
//			String encodedNewPassword = passwordEncoder.encode(adminUpdate.getNewPassword());
//			user.setPassword(encodedNewPassword);
//		}
//		user.setEmail(adminUpdate.getEmail());
//		Set<Role> roles = roleService.findRoleByNames(adminUpdate.getRoles());
//		user.setRoles(roles);
//		userRepo.save(user);
//	}
		
		
	
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
