package com.jhaps.clientrecords.service.system;



import com.jhaps.clientrecords.dto.request.user.UserRegisterRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
import com.jhaps.clientrecords.dto.request.user.UserUpdateRequest;
import com.jhaps.clientrecords.dto.response.user.UserGeneralResponse;
import com.jhaps.clientrecords.entity.system.User;

public interface UserService {
	
	User getCurrentUser(int userId);
	
	User findUserById(int id); /* Return type is "User" | It is used for internal business logic --( Service classes )--*/ 
	
	User saveUser(User user); /* No Logic just saving the user that is ready. */
	
	void saveNewUser(UserRegisterRequest userRegisterRequest); /* Saving/registering new user with logic. */
	
	void deleteCurrentUser(int userId);
	
	void updateCurrentUser(int userId, UserUpdateRequest userUpdateInfo);
	
//	void updateCurrentUserProfileImage(int id, UserImageUploadRequest request);
	
	String updateCurrentUserProfileImage(int id, UserImageUploadRequest request);
	
	void removeCurrentUserProfileImage(int userId); 
	
	
}//ends interface.
