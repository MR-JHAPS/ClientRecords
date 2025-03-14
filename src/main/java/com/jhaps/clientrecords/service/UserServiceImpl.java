package com.jhaps.clientrecords.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepo;
	private RoleService roleService;
	private JWTServiceImpl jwtServiceImpl;
	private AuthenticationManager authManager;	//custom Authentication manager from securityConfig.
	private PasswordEncoder passwordEncoder;
	private Mapper mapper;
	
	public UserServiceImpl(UserRepository userRepo, RoleService roleService, 
							JWTServiceImpl jwtServiceImpl, AuthenticationManager authManager,
							Mapper mapper, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.roleService = roleService;
		this.jwtServiceImpl = jwtServiceImpl;
		this.authManager = authManager;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}

	
	//FOR VERIFICATION -- USER LOGIN:
//	@Override
//	public String verifyUser(UserDto userDto) {
//		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
//		if(auth.isAuthenticated()) {
//			return jwtServiceImpl.generateJWTToken(userDto.getEmail());
//		}
//		return "failed to authenticate";
//	}

	
	@Override
	public String verifyUser(UserDto userDto) {
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
		if(!auth.isAuthenticated()) {
			throw new BadCredentialsException( "Wrong authentication/credentials Details in Email : " + userDto.getEmail() );
		}
		return jwtServiceImpl.generateJWTToken(userDto.getEmail());
	}
	
	
	
	@Override
	public Page<UserDto> findAllUsers(Pageable pageable) {
		Page<User> userList = userRepo.findAll(pageable);
		if(userList.getContent().isEmpty()) {
			throw new UserNotFoundException("No users Found in the Database");
		}
		return userList.map(mapper::toUserDto);
	}
	
	
	
	@Override
	public UserDto findUserDtoById(int id) {
		User user = userRepo.findById(id).orElseThrow( ()->
		 					new UserNotFoundException("Unable to find the user with ID : " + id) );
		return mapper.toUserDto(user);	
	}


	
	@Override
	public User findUserById(int id) {
		User user = userRepo.findById(id).orElseThrow( ()->
		 					new UserNotFoundException("Unable to find the user with ID : " + id) );
		return user;	
	}
	
	
	
	//Email field is unique so instead of list it returns only single User.
	@Override
	public UserDto findUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(()->
						 new UserNotFoundException("Unable to find the user with Email : " + email));
		return mapper.toUserDto(user);					
	}

	public boolean isRoleValid(String roleName) {
		boolean isFound = false;
		for(RoleNames role : RoleNames.values()) {
			if(roleName.equals(role.getRole())) {
				 isFound=true;
				 break;
			}
		}//ends for-Loop.
		return isFound;
	}
	
//	public boolean isRoleValid(String roleName) {
//		return Arrays.stream(RoleNames.values())
//					 .anyMatch(role->
//					 			role.getRole().equals(roleName));
//	}

	
	@Transactional
	@Override
	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable) {
		boolean isRoleFound = isRoleValid(roleName); //using isRoleValid() method written above to see if role is valid in RoleNames.enum
		if(!isRoleFound) {
			throw new RoleNotFoundException("Unable to find Role with name : " + roleName);	
		}
		Page<User> userList = userRepo.findByRole_Name(roleName, pageable);
		if(userList.getContent().isEmpty()) {
			throw new UserNotFoundException("No users found in Database with given Role");
		}
		return userList.map(mapper::toUserDto);
	}

	
	
	@Override
	public void saveUser(UserDto userDto) {
		if(userRepo.existsByEmail(userDto.getEmail())) {
			throw new DuplicateDataException("Unable to save user, User with email : " + userDto.getEmail() + " already exists.");
		}
		User user = mapper.toUserEntity(userDto); //changing dto to entity
		Role defaultRole = roleService.findRoleByName(RoleNames.USER.getRole()).orElseThrow(()->
							new RoleNotFoundException("Unable to find the Role :"+ RoleNames.USER));//Setting the defaultRole as 'user'.
		String encodedPassword = passwordEncoder.encode(user.getPassword()); //encrypting/encoding Password
		user.setPassword(encodedPassword);
		user.setRole(Collections.singleton(defaultRole));	
		userRepo.save(user);	
	}
	
	
	
//	@Transactional
//	@Override
//	//Auth contains the login Info of the active user.
//	public void deleteUserById(int id, Authentication auth) { 
//		UserDetails activeEmail = (UserDetails)auth.getPrincipal().
//		User user = findUserById(id); //findUserById() is a method written above.
//		if(user.getEmail().equals(auth.) || auth.getAuthorities().contains(RoleNames.ADMIN.getRole())) {
//			userRepo.delete(user);
//		}else {
//			throw new AccessDeniedException("You don't have permission required to perform the operation");
//		}
//		
////		userRepo.delete(user);
//	}//ends method.
	
	
//	//Auth contains the login Info of the active user.
//	@Transactional
//	@Override
//	public void deleteUserById(int id, Authentication auth) throws AccessDeniedException { 
//		
//		CustomUserDetails customUserDetails = (CustomUserDetails)auth.getPrincipal();
//		String loggedInUser = customUserDetails.getUsername();
//		
//		Collection<? extends GrantedAuthority> roles = (Collection<? extends GrantedAuthority>) customUserDetails.getAuthorities(); 
//		List<String> rolesList = roles.stream().map(role-> role.toString()).collect(Collectors.toList());
//		
//		User user = findUserById(id); //findUserById() is a method written above.
//		if(user.getEmail().equals(loggedInUser) || rolesList.contains(RoleNames.ADMIN.getRole())) {
//			userRepo.delete(user);
//		}else {
//			throw new AccessDeniedException("You don't have permission required to perform the operation");
//		}
//	}//ends method.
	
	
	//Auth contains the login Info of the active user.
	@Transactional
	@Override
	public void deleteUserById(int id) throws AccessDeniedException { 
		//Getting AuthenticationDetails of Logged in User from "SecurityContextHolder".
		CustomUserDetails customUserDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String loggedInUser = customUserDetails.getUsername();
		Set<String> roles = customUserDetails.getAuthorities()
										.stream()
										.map(GrantedAuthority::getAuthority) //converting To String from Collections.
										//OR .map(role->role.toString())
										.collect(Collectors.toSet());
		
		User user = findUserById(id); //findUserById() is a method written above.
		if(user.getEmail().equals(loggedInUser) || roles.contains(RoleNames.ADMIN.getRole())) {
			userRepo.delete(user);
		}else {
			throw new AccessDeniedException("You don't have permission required to perform the operation");
		}
	}//ends method.
	
	
	
	
	
	
	
	
	@Transactional
	@Override
	public void updateUserById(int id, UserDto userUpdateInfo) {
		User user = findUserById(id);   //findUserById() is a method written above.
		user.setEmail(userUpdateInfo.getEmail());
		user.setPassword(passwordEncoder.encode(userUpdateInfo.getPassword())); //encoding and setting new password.
		userRepo.save(user);
	}






	
	
}//ends class
