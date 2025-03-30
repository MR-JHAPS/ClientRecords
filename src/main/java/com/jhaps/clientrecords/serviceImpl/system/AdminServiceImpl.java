package com.jhaps.clientrecords.serviceImpl.system;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jhaps.clientrecords.dto.request.user.AdminUpdate;
import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.dto.response.UserDto;
import com.jhaps.clientrecords.dto.response.user.UserAdmin;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.RoleNames;
import com.jhaps.clientrecords.exception.system.RoleNotFoundException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.security.customAuth.PasswordValidator;
import com.jhaps.clientrecords.service.system.AdminService;
import com.jhaps.clientrecords.service.system.RoleService;
import com.jhaps.clientrecords.service.system.UserService;
import com.jhaps.clientrecords.util.Mapper;
import com.jhaps.clientrecords.util.SecurityUtils;
import com.jhaps.clientrecords.util.UserMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{

	private RoleService roleService;
	private UserRepository userRepo;
	private SecurityUtils securityUtils;
	private Mapper mapper;
	private UserMapper userMapper;
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	private PasswordValidator passwordValidator;
	
	
	
	@Override
	public Page<UserAdmin> findAllUsers(Pageable pageable) {
		Page<User> userList = userRepo.findAll(pageable);
		if(userList.getContent().isEmpty()) {
			throw new UserNotFoundException("No users Found in the Database");
		}
		log.info("Finding All Users is Executed Successfully. and fetched :{} clients", userList.getNumberOfElements());
		return userList.map(userMapper::toUserAdminDto);
	}
	
	
	/* Returns "UserAdminDto" this contains userRoles to view in admin-Dashboard */
	public UserAdmin findUserWithRolesById(int id) {
		User user = userRepo.findById(id).orElseThrow( ()->
			new UserNotFoundException("Unable to find the user with ID : " + id) );
		return userMapper.toUserAdminDto(user);
	}
	
	
	@Override
	public void updateUserRoleById(int id, RoleDto roleDto) {
		User user = userService.findUserById(id); 
		Set<String> activeUserRoles = securityUtils.getAuthoritiesFromCustomUserDetails(); //getting the roles of the logged in user.
		
		if(!activeUserRoles.contains(RoleNames.ADMIN.getRole())) {
			throw new AccessDeniedException("You do not have authorization to update the role");
		}
		Set<String> roleNamesFromDto = roleDto.getRoleNames(); 
		log.info("updating role of User with id: {} with roles :{}", id, roleDto.getRoleNames());
		//checking if the roles exists in the roleRepository.
		Set<Role> roles = roleService.findRoleByNames(roleNamesFromDto);
		user.getRoles().clear(); // clearing previous roles
		user.setRoles(roles); // setting new roles.
		userRepo.save(user);
		log.info("User Role of id :{} Updated successfully",id);
	}
	
	
	//Getting the list of user by their role|Authority.
	@Transactional
	@Override
	public Page<UserAdmin> findUsersByRoleName(String roleName, Pageable pageable) {
		//checking if the argument roleName is valid.
		boolean isRoleFound = roleService.isRoleValid(roleName); 
		if(!isRoleFound) {
			throw new RoleNotFoundException("Unable to find Role with name : " + roleName);	
		}
		Page<User> userList = userRepo.findByRoles_Name(roleName, pageable);
		if(userList.getContent().isEmpty()) {
			throw new UserNotFoundException("No users found in Database with given Role");
		}
		return userList.map(userMapper::toUserAdminDto); //mapping user to userAdmin
	}



	@Override
	public void updateAdmin(String currentUserEmail, AdminUpdate adminUpdate) {
		User user = userService.findUserByEmail(currentUserEmail); //getting current User from securityContextHolder
		// For Safety: to check if the user has Role: admin. 
		Set<String> userRoles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		if(!userRoles.contains(RoleNames.ADMIN.getRole())) {
			throw new AccessDeniedException("Error: You are not admin and cannot update this account");
		}
		/* If "current-password" does not match the "encoded-account-password" it will throw exception*/
		passwordValidator.verifyCurrentPassword(adminUpdate.getCurrentPassword(), user.getPassword());
		
		/* Checking if user is asking to change a password*/
		if(StringUtils.hasText(adminUpdate.getNewPassword())) {
			/* throws error if newPassword and confirmPassword does not match. */
			passwordValidator.validatePasswordMatch(adminUpdate.getNewPassword(), adminUpdate.getConfirmPassword()); 
			String encodedNewPassword = passwordEncoder.encode(adminUpdate.getNewPassword());
			user.setPassword(encodedNewPassword);
		}
		user.setEmail(adminUpdate.getEmail());
		Set<Role> roles = roleService.findRoleByNames(adminUpdate.getRoles());
		user.setRoles(roles);
		userRepo.save(user);
	}


	
	@Override
	@Transactional
	public void deleteUserById(int id) {
		User user = userService.findUserById(id);
		userRepo.delete(user);
	}
		
		
	
}// ends class.
