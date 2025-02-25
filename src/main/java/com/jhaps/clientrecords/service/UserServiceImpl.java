package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.Role;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.exception.DuplicateDataException;
import com.jhaps.clientrecords.exception.EntityNotFoundException;
import com.jhaps.clientrecords.repository.UserRepository;
import com.jhaps.clientrecords.util.Mapper;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepo;
	private RoleService roleService;
	private JWTServiceImpl jwtServiceImpl;
	private AuthenticationManager authManager;
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
	@Override
	public String verifyUser(UserDto userDto) {
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
		if(auth.isAuthenticated()) {
			return jwtServiceImpl.generateJWTToken(userDto.getEmail());
		}
		return "failed to authenticate";
	}

	
	@Override
	public Page<UserDto> findAllUsers(Pageable pageable) {
		Page<User> userList = userRepo.findAll(pageable);
		return userList.map(mapper::toUserDto);
	}
	
	
	@Override
	public Optional<UserDto> findUserById(int id) {
		return userRepo.findById(id)
					.map(mapper::toUserDto);	
	}

	
	@Override
	public Optional<UserDto> findUserByEmail(String email) {
		return userRepo.findByEmail(email)
						.map(mapper::toUserDto);	
	}

	
	@Override
	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable) {
		Page<User> userList = userRepo.findByRole_Name(roleName, pageable);
		return userList.map(mapper::toUserDto);
	}
	
	
	@Override
	public void saveUser(UserDto userDto) {
		if(userRepo.existsByEmail(userDto.getEmail())) {
			throw new DuplicateDataException(userDto.getEmail());
		}
		User user = mapper.toUserEntity(userDto); //changing dto to entity
		//encrypting/encoding Password
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		//Setting the default Role as user.
		Role defaultRole = roleService.findRoleByName("user").orElseThrow(()-> new EntityNotFoundException("In UserService saveUser() setting Default role", "' user'"));
		user.setRole(defaultRole);	
		userRepo.save(user);	
	}

	
	@Override
	public void deleteUserById(int id) {
		if(!userRepo.existsById(id)) {
			throw new UsernameNotFoundException("user with "+id + " not found");
		}
		userRepo.deleteById(id);
	}//ends method.
	
	
	@Override
	public void updateUserById(int id) {
	
	}



	
	

	
	
	
	
	
	
	
	
	
	
	
	
}//ends class
