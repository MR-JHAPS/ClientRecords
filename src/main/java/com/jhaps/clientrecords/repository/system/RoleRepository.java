package com.jhaps.clientrecords.repository.system;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.enums.RoleNames;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Set<Role>> findByNameIn(Set<String> roleNames);//for multiple Role findByNameIn (In) is necessary.
	
	Optional<Role> findByName(String roleName); //for single role
	
//	Optional<Set<Role>> findAll();
	
	
}
