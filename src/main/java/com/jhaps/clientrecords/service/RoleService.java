package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.jhaps.clientrecords.dto.RoleDto;
import com.jhaps.clientrecords.entity.Role;

public interface RoleService {

	public Optional<Role> findRoleById(int id);
	
	public Optional<Role> findRoleByName(String name); //for single role.
	
	public Optional<Set<Role>> findRoleByName(Set<String> roleNames); //for multiple role.
	
	public Set<RoleDto> findAllRoles();
}
