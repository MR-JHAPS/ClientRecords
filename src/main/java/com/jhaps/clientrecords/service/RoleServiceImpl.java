package com.jhaps.clientrecords.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.Role;
import com.jhaps.clientrecords.enums.RoleNames;
import com.jhaps.clientrecords.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepo;

	
	
	
	@Override
	public Optional<Role> findRoleById(int id) {
		return roleRepo.findById(id);
	}

	@Override
	public Optional<Role> findRoleByName(RoleNames roleName) {
		return roleRepo.findByName(roleName);
	}
	
	
	
	
	
	
	
	
}
