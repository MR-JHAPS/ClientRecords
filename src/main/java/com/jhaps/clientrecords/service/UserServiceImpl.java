package com.jhaps.clientrecords.service;

import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.UserDto;
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
		Role defaultRole = roleService.findRoleByName(RoleNames.USER).orElseThrow(()->
							new RoleNotFoundException("Unable to find the Role :"+ RoleNames.USER));//Setting the defaultRole as 'user'.
		String encodedPassword = passwordEncoder.encode(user.getPassword()); //encrypting/encoding Password
		user.setPassword(encodedPassword);
		user.setRole(defaultRole);	
		userRepo.save(user);	
	}
	
	
	
	@Transactional
	@Override
	public void deleteUserById(int id) {
		User user = findUserById(id); //findUserById() is a method written above.
		userRepo.delete(user);
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
