package com.jhaps.clientrecords.util.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;
import com.jhaps.clientrecords.dto.request.user.UserRegisterRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
import com.jhaps.clientrecords.dto.response.user.UserAdminResponse;
import com.jhaps.clientrecords.dto.response.user.UserGeneralResponse;
import com.jhaps.clientrecords.entity.system.User;

@Component
public class UserMapper {

	
	/*-------------------------------------------------- TO Entity ----------------------------------------------------------------------*/	
	
	
	
	/* we are just getting the email.
	 * Password, roles others will be
	 * handled in service class.
	 * */
	public  User toUserEntity(UserRegisterRequest dto) {
		User user = new User();
		user.setEmail(dto.getEmail());
		return user;
	}
	
	
	public User toUserEntity(UserAuthRequest dto) {
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		return user;
	}
	
	
	
	/*-------------------------------------------------- TO DTO's ----------------------------------------------------------------------*/	
	
	public UserGeneralResponse toUserGeneralResponse(User user) {
		UserGeneralResponse dto = new UserGeneralResponse();
		dto.setId(user.getId());
		dto.setImageUrl(user.getProfileImageUrl());
		dto.setEmail(user.getEmail());
		dto.setCreatedOn(user.getCreatedOn());
		dto.setUpdatedOn(user.getUpdatedOn());
		return dto;
	}
	
	
	public UserAdminResponse toUserAdminResponse(User user) {
		UserAdminResponse dto = new UserAdminResponse();
		dto.setId(user.getId());
		dto.setProfileImageUrl(user.getProfileImageUrl());
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
	
	
	
	
/*----------------------FOR PROFILE PICTURE-------------------------------------------------------------*/
	
	/* For profilePicture update of User we need to convert :
	 * 	UserUpdateProfile To ImageRequest 
	 */
	
	public ImageRequest toImageRequestFromUserUpdateImage(UserImageUploadRequest userImageUploadRequest) {
		ImageRequest imageRequest = new ImageRequest();
		imageRequest.setImageName(userImageUploadRequest.getImageName());
		return imageRequest;
	}
	
	
	
	
	
}//ends class
