package com.jhaps.clientrecords.service.system;



import com.jhaps.clientrecords.dto.request.user.UserRegisterRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
import com.jhaps.clientrecords.dto.request.user.UserUpdateRequest;
import com.jhaps.clientrecords.dto.response.user.UserGeneralDto;
import com.jhaps.clientrecords.entity.system.User;

public interface UserService {
	
	User findUserById(int id); /* Return type is "User" | It is used for internal business logic --( Service classes )--*/ 
	
	UserGeneralDto findUserDtoByEmail(String email); /* Returns "UserGeneralDto" this is for User-dashboard.*/
	
	User findUserByEmail(String email);
	
	void saveUser(User user); /* No Logic just saving the user that is ready. */
	
	void saveNewUser(UserRegisterRequest userRegisterRequest); /* Saving/registering new user with logic. */
	
	void deleteUserByEmail(String email) ;
	
	void updateUserByEmail(String email, UserUpdateRequest userUpdateInfo);
		
	void updateUserProfileImage(String email, UserImageUploadRequest request);
	
	
	
	
}
