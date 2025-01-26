package com.jhaps.clientrecords.service;

import java.util.Optional;

import com.jhaps.clientrecords.entity.Role;

public interface RoleService {

	public Optional<Role> findRoleById(int id);
	
	public Optional<Role> findRoleByName(String roleName);
	
}
