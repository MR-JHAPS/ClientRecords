package com.jhaps.clientrecords.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.LoginAttempts;
import com.jhaps.clientrecords.dto.RoleDto;
import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.CustomUserDetails;
import com.jhaps.clientrecords.entity.Role;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.enums.RoleNames;
import com.jhaps.clientrecords.exception.DuplicateDataException;
import com.jhaps.clientrecords.exception.RoleNotFoundException;
import com.jhaps.clientrecords.exception.UserNotFoundException;
import com.jhaps.clientrecords.repository.UserRepository;
import com.jhaps.clientrecords.util.Mapper;
import com.jhaps.clientrecords.util.SecurityUtils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

	private UserRepository userRepo;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder;
	private Mapper mapper;
	
	public UserServiceImpl(UserRepository userRepo, RoleService roleService, 
							Mapper mapper, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.roleService = roleService;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}

	
//-------------------------------LOGIN VERIFICATION----------------------------------------------------------------------------------------------------------	

	
	/*THIS IS TO UPDATE THE USER LOGIN ATTEMPTS*/
	@Override
	public void updateLoginAttempts(UserDto userDto) {
		String email = userDto.getEmail(); //email of user who made wrong password attempt
		User user = userRepo.findByEmail(email)
					.orElseThrow(()-> new UserNotFoundException("Unable to find User with Email : " + email));
		int previousAttempts = user.getAttempts(); /* Getting the user wrong password attempts from the Database if exists */
		int currentAttempts = previousAttempts + 1 ;  /* current wrong password attempts*/
		user.setAttempts(currentAttempts);		/* Saving the current wrong password attempts in the user Database.*/
		if(currentAttempts >= 3) {				/* If the wrong attempts equals 3 or more than 3 attempts the account will be locked along with TimeStamp*/
			user.setAccountLocked(true); 
			user.setLockTime(LocalDateTime.now());
			throw new LockedException("Your Account is locked. Wait 15 minutes.");
		}
		userRepo.save(user);
	}
	
	
	
//-------------------------------THESE ARE CRUD----------------------------------------------------------------------------------------------------------	
	@Override
	public Page<UserDto> findAllUsers(Pageable pageable) {
		Page<User> userList = userRepo.findAll(pageable);
		if(userList.getContent().isEmpty()) {
			throw new UserNotFoundException("No users Found in the Database");
		}
		log.info("Finding All Users is Executed Successfully. and fetched :{} clients", userList.getNumberOfElements());
		return userList.map(mapper::toUserDto);
	}
	
	
	/* Returns "UserDto" specially used for Controller.*/
	@Override			
	public UserDto findUserDtoById(int id) {
		User user = userRepo.findById(id).orElseThrow( ()->
		 					new UserNotFoundException("Unable to find the user with ID : " + id) );
		return mapper.toUserDto(user);	
	}
	
	
	/* Return type is "User" | It is used for internal business logic --( Service classes )--*/
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
	public void saveNewUser(UserDto userDto) {
		if(userRepo.existsByEmail(userDto.getEmail())) {
			throw new DuplicateDataException("Unable to save user, User with email : " + userDto.getEmail() + " already exists.");
		}
		User user = mapper.toUserEntity(userDto); //changing dto to entity
		
		log.info("Saving new User with default Role: {}", RoleNames.USER.getRole());
		Role defaultRole = roleService.findRoleByName(RoleNames.USER.getRole()); //error is handled in roleService.
		String encodedPassword = passwordEncoder.encode(user.getPassword()); //encrypting/encoding Password
		user.setPassword(encodedPassword);
		user.setRoles(Set.of(defaultRole));	//since the Role of user is of type Set<Role> setting the default value as set.of('user')
		user.setAccountLocked(false);
		user.setAttempts(0);
		saveUser(user);	
	}
	
	
	
	/* --'SecurityUtils'-- contains the login Info of the active user. */
	@Transactional
	@Override
	public void deleteUserById(int id) throws AccessDeniedException { 
		String loggedInUser = SecurityUtils.getEmailFromCustomUserDetails(); //this is the private method written above.
			log.info("user Email extracted from the userServiceImpl is {}",loggedInUser);
		Set<String> roles = SecurityUtils.getAuthoritiesFromCustomUserDetails(); //this is the private method written above.
			log.info("Current user roles are : {}", roles);
		User user = findUserById(id); //findUserById() is a method written above.
		//Only the owner of the Account or Admin will be able to delete the userAccount.
		if(user.getEmail().equals(loggedInUser) || roles.contains(RoleNames.ADMIN.getRole())) {
			userRepo.delete(user);
		}else {
			throw new AccessDeniedException("You don't have permission required to perform the operation");
		}
	}//ends method.
	
	
	
	//To update the user you also need to provide the current password for verification purpose.
	@Transactional
	@Override
	public void updateUserById(int id, UserDto userUpdateInfo) {
		User user = findUserById(id);   //findUserById() is a method written above.
		//This update permission is for the logged in user.(No permission to change the role)
		String loggedInUser = SecurityUtils.getEmailFromCustomUserDetails(); //getting logged in email from private method above.

		if(!loggedInUser.equalsIgnoreCase(user.getEmail())) {
			throw new AccessDeniedException("You are not authorized to update this account of id " + id);
		}
		user.setEmail(userUpdateInfo.getEmail());
		user.setPassword(passwordEncoder.encode(userUpdateInfo.getPassword())); //encoding and setting new password.
		userRepo.save(user);
	}

	
	
	//THIS IS ONLY PERMITTED TO THE ADMIN.
	@Override
	public void updateUserRoleById(int id, RoleDto roleDto) {
		User user = findUserById(id); //this is the private method written above.
		Set<String> activeUserRoles = SecurityUtils.getAuthoritiesFromCustomUserDetails(); //getting the roles of the logged in user.
		
		if(!activeUserRoles.contains(RoleNames.ADMIN.getRole())) {
			throw new AccessDeniedException("You do not have authorization to update the role");
		}
		Set<String> roleNamesFromDto = roleDto.getRoleNames(); // this contains the Set<String> of roles from front-end.
//		boolean isRoleValid = isRoleValid(roleNamesFromDto);
//		if(!isRoleValid) {
//			throw new RoleNotFoundException("Unable to find the Role " + roleNamesFromDto);
//		}
		//getting the Set of roles from the Database. 
		log.info("updating role of User with id: {} with roles :{}", id, roleDto.getRoleNames());
		Set<Role> roles = roleService.findRoleByNames(roleNamesFromDto);
		user.getRoles().clear();
		user.setRoles(roles);
		userRepo.save(user);
		log.info("User Role of id :{} Updated successfully",id);
	}//ends method

	
	
	
	//Email field is unique so instead of list it returns only single User.
	@Override
	public UserDto findUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(()->
						 new UserNotFoundException("Unable to find the user with Email : " + email));
		return mapper.toUserDto(user);					
	}


	
	//Getting the list of user by their role|Authority.
	@Transactional
	@Override
	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable) {
		//checking if the argument roleName is valid before proceeding to Database.
		boolean isRoleFound = roleService.isRoleValid(roleName); //using isRoleValid() method written above to see if role is valid in RoleNames.enum
		if(!isRoleFound) {
			throw new RoleNotFoundException("Unable to find Role with name : " + roleName);	
		}
		Page<User> userList = userRepo.findByRoles_Name(roleName, pageable);
		if(userList.getContent().isEmpty()) {
			throw new UserNotFoundException("No users found in Database with given Role");
		}
		return userList.map(mapper::toUserDto);
	}


	
/*---------------------------------------- LOGIN ATTEMPTS --------------------------------------------------------------------------------*/	
	

	
	@Override
	public boolean unlockAfterGivenTime(User user) {
		if(user.isAccountLocked() && user.getLockTime()!=null) {
			LocalDateTime unlockTime = user.getLockTime().plusMinutes(15);
			if(LocalDateTime.now().isAfter(unlockTime)) {  // if current time is 15 minutes after user.getLockTime
				user.setAttempts(0);
				user.setAccountLocked(false);
				user.setLockTime(null);
				userRepo.save(user);
				return true;
			}
		}
		return false;	
	}
	
	
	
	
	@Override
	public void resetLoginAttempts(LoginAttempts loginAttempts) {
		String email = loginAttempts.getEmail();
		int attempts = loginAttempts.getAttempts();
		User user = userRepo.findByEmail(email)
				.orElseThrow(()-> new UserNotFoundException("Unable to find User with Email : " + email));
		
		user.setAttempts(0);
		userRepo.save(user);
	}



	/* Unlock the locked User Account */
	@Override
	public void unlockAccount(int id) {
		User user = findUserById(id);
		user.setAccountLocked(false);
		saveUser(user);
	}



	/* Lock the User Account */
	@Override
	public void lockAccount(int id) {
		User user = findUserById(id);
		user.setAccountLocked(true);
		saveUser(user);
	}

	
	


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
			



	
	
}//ends class
