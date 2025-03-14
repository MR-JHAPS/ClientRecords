package com.jhaps.clientrecords.service;

import java.util.Optional;
import java.util.Set;

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
	public Optional<Set<Role>> findRoleByName(Set<String> roleNames) {
		return roleRepo.findByNameIn(roleNames);
	}

	@Override
	public Optional<Role> findRoleByName(String name) {
		return roleRepo.findByName(name);
	}
	
	
	
	
	
	
	
	
}
