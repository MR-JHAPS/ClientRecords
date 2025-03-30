package com.jhaps.clientrecords.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.request.user.UserAuth;
import com.jhaps.clientrecords.dto.request.user.UserRegister;
import com.jhaps.clientrecords.dto.response.UserDto;
import com.jhaps.clientrecords.dto.response.user.UserAdmin;
import com.jhaps.clientrecords.dto.response.user.UserGeneralDto;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.entity.system.User;

@Component
public class UserMapper {

	
	/*-------------------------------------------------- TO Entity ----------------------------------------------------------------------*/	
	
	
	
	/* we are just getting the email.
	 * Password, roles others will be
	 * handled in service class.
	 * */
	public  User toUserEntity(UserRegister dto) {
		User user = new User();
		user.setEmail(dto.getEmail());
		return user;
	}
	
	
	public User toUserEntity(UserAuth dto) {
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		return user;
	}
	
	
	
	/*-------------------------------------------------- TO DTO's ----------------------------------------------------------------------*/	
	
	public UserGeneralDto toUserGeneralDto(User user) {
		UserGeneralDto dto = new UserGeneralDto();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setCreatedOn(user.getCreatedOn());
		dto.setUpdatedOn(user.getUpdatedOn());
		return dto;
	}
	
	
	public UserAdmin toUserAdminDto(User user) {
		UserAdmin dto = new UserAdmin();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setCreatedOn(user.getCreatedOn());
		dto.setUpdatedOn(user.getUpdatedOn());
		/* Getting the list of roles of user Set<Role> and converting it to Set<String>
		 * " role of User_Entity is of type set<Role> "  
		 *   and 
		 * " role type in the UserAdmin_dto. Set<String> "
		 * */
		Set<String> roles = user.getRoles().stream().map(role->role.getName()).collect(Collectors.toSet());
		dto.setRoles(roles);
		return dto;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}//ends class
