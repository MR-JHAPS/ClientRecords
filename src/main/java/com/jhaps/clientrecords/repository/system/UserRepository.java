package com.jhaps.clientrecords.repository.system;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.system.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	boolean existsByEmail(String email);
	
	Page<User> findAll(Pageable pageable);
	
	/* This is for general usages(authentication, internal uses('Business logic')). */
	Optional<User> findByEmail(String email); 
	
	
	/* This is only for the search user By userEmail. Just for the searching of the user using email. */
	@Query("SELECT u from User u WHERE u.email LIKE(CONCAT('%',:p_email,'%'))")
	Optional<User> findUserByEmail(@Param("p_email") String email); 
	
	
	//In findByRole_Name "_" it is used for nested property like 'User.Role.Name' OR 'User-->Role-->Name'
	Page<User> findByRoles_Name(String roleName, Pageable pageable);
	
	Page<User> findByRoles_Name(Set<String> roleName, Pageable pageable);//finding using mulitple roleNames

	
	
	
	
}
