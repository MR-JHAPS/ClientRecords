package com.jhaps.clientrecords.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	boolean existsByEmail(String email);
	
	Page<User> findAll(Pageable pageable);
	
	Optional<User> findByEmail(String email);
	
	//In findByRole_Name "_" it is used for nested property like 'User.Role.Name' OR 'User-->Role-->Name'
	Page<User> findByRole_Name(String roleName, Pageable pageable);
	
	Page<User> findByRole_Name(Set<String> roleName, Pageable pageable);//finding using mulitple roleNames
	
}
