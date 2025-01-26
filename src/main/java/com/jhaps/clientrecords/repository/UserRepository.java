package com.jhaps.clientrecords.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String email);
	
	//In findByRole_Name "_" it is used for nested property like 'User.Role.Name' OR 'User-->Role-->Name'
	List<User> findByRole_Name(String roleName);
	
}
