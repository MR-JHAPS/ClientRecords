package com.jhaps.clientrecords.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private PasswordEncoder passwordEncoder;
	private UserService userService;	
	
	public UserController(PasswordEncoder passwordEncoder, UserService userService) {
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}



	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		
		List<User> userList = userService.findAllUsers();
		
		if(userList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}	
		return ResponseEntity.ok(userList);
		
	}
	
	
//	@GetMapping("/id/{id}")
//	public ResponseEntity<User> getUserById(@PathVariable int id) {
//		
//		Optional<User> user = userService.findUserById(id);
//		
//		//optional is never null so just checking for empty/present.
//		if(user.isEmpty() ) {
//			return ResponseEntity.notFound().build();
//		}	else {
//			//if user.isPresent() then we will do user.get() because user-->is optional a return type is just a user.
//			//converting from optionalUser to user.
//		return ResponseEntity.ok(user.get());
//		}
//	}  //OR
	

	@GetMapping("/id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable int id) {
	
		return userService.findUserById(id)
					.map(ResponseEntity::ok)
					.orElse(ResponseEntity.notFound().build());
	}
	
	
	
	@GetMapping("/role/{role}")
	public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role){
		
		List<User> userList = userService.findUsersByRoleName(role);
		if(userList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(userList);
		
		
	}
	
//	@GetMapping("/email/{email}")
//	public ResponseEntity<User> getUsersByEmail(@PathVariable String email){
//		
//		return userService.findUsersByRoleName(email)
//				.map(ResponseEntity::ok)
//				.orelse(ResponseEntity.notFound().build());
//	}
	
	
	
}// ends class
