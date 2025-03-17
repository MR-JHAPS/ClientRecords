package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.RoleDto;
import com.jhaps.clientrecords.entity.Role;
import com.jhaps.clientrecords.enums.RoleNames;
import com.jhaps.clientrecords.repository.RoleRepository;
import com.jhaps.clientrecords.util.Mapper;

@Service
public class RoleServiceImpl implements RoleService {

	private RoleRepository roleRepo;
	
	private Mapper mapper;

	public RoleServiceImpl(RoleRepository roleRepo, Mapper mapper) {
		this.roleRepo = roleRepo;
		this.mapper = mapper;
	}
	
	
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

	@Override
	public Set<RoleDto> findAllRoles() {
		List<Role> roleList = roleRepo.findAll();
		return roleList.stream().map(m)
	}
	
	
	
	
	
	
	
	
}
