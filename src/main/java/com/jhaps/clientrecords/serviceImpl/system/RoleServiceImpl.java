package com.jhaps.clientrecords.serviceImpl.system;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.request.RoleSaveRequest;
import com.jhaps.clientrecords.dto.response.RoleResponse;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.enums.RoleNames;
import com.jhaps.clientrecords.exception.system.RoleNotFoundException;
import com.jhaps.clientrecords.repository.system.RoleRepository;
import com.jhaps.clientrecords.service.system.RoleService;
import com.jhaps.clientrecords.util.mapper.RoleMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

	private RoleRepository roleRepo;
	private RoleMapper roleMapper;
	

	public RoleServiceImpl(RoleRepository roleRepo, RoleMapper roleMapper) {
		this.roleRepo = roleRepo;
		this.roleMapper = roleMapper;
	}
	
	
	

	@Override
	public Role findRoleById(int id) {
		return roleRepo.findById(id)
						.orElseThrow(()->
							new RoleNotFoundException("Unable to find the Role with id : "+ id));
	}

	
	
	@Override
	public Set<Role> findRoleByNames(Set<String> roleNames) {
		Set<Role> roleSet = roleRepo.findByNameIn(roleNames)
								.orElseThrow(()->
										new RoleNotFoundException("Unable to find the roles with names : " + roleNames));
		return roleSet;
	}

	
	
	@Override
	public Role findRoleByName(String name) {
		Role role = roleRepo.findByName(name)
						.orElseThrow(()->
								new RoleNotFoundException("Unable to find Role with name : " + name));
		return role; //converting Role to RoleDto
	}

	
	
//	@Override
//	public Set<RoleResponse> findAllRoles() {
//		log.info("Getting the List of all the Roles.");
//		List<Role> roleList = roleRepo.findAll();
//		if(roleList.isEmpty()) {
//			throw new RoleNotFoundException("Unable to find any roles in the Database.");
//		}
//		return roleList.stream().map(roleMapper::toRoleResponse).collect(Collectors.toSet());
//	}
	
	
	@Override
	public Set<Role> findAllRoles() {
		log.info("Getting the List of all the Roles.");
		List<Role> roleList = roleRepo.findAll();
		if(roleList.isEmpty()) {
			throw new RoleNotFoundException("Unable to find any roles in the Database.");
		}
		return roleList.stream().collect(Collectors.toSet());
//		return roleList.stream().map(roleMapper::toRoleResponse).collect(Collectors.toSet());
	}





	@Override
	public void saveNewRole(RoleSaveRequest roleSaveRequest) {
		String roleName = roleSaveRequest.getRole(); //converting Set<String> to String
		 Role role = new Role(0, roleName);	//saving new role.
		 roleRepo.save(role);
		
	}
	
	@Override
	public void deleteRole(int id) {
		log.info("Deleting Role with id: {}", id);
		Role role = roleRepo.findById(id)
						.orElseThrow(()->
								new RoleNotFoundException("Role with id : " + id + " not found"));
		log.info("Role with Id: {} deleted Successfully");
		roleRepo.delete(role);
	}
	
	

	
	
	/* This is for the validation of Set<String> roles. (Multiple Roles at once) */
	public boolean isRoleValid(Set<String> roleNames) {
		Set<String> validRoles = Arrays.stream(RoleNames.values())
								.map(RoleNames::getRole)
								.collect(Collectors.toSet());
		
		return roleNames.stream()
						.allMatch(r->validRoles.contains(r));
	}
	
	/* Checking if the role exists in the RoleNames.enum.(Single Role) */
	public boolean isRoleValid(String roleName) {
		boolean isFound = false;
		for(RoleNames role : RoleNames.values()) {
			if(roleName.equals(role.getRole())) {
				 isFound=true;
				 break;
			}
		}//ends for-Loop.
		return isFound;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
// --------------------------------------------THE CODES DOWN FROM HERE HAS RETURN TYPE OF ROLEDTO  --------------------------------------------------------------
	
	
//	
//	@Override
//	public RoleDto findRoleById(int id) {
//		Role role = roleRepo.findById(id)
//						.orElseThrow(()->
//							new RoleNotFoundException("Unable to find the Role with id : "+ id));
//		return mapper.toRoleDtoFromSingleRole(role); //converting role to RoleDto
//	}
//
//	
//	
//	@Override
//	public RoleDto findRoleByNames(Set<String> roleNames) {
//		Set<Role> roleSet = roleRepo.findByNameIn(roleNames)
//								.orElseThrow(()->
//										new RoleNotFoundException("Unable to find the roles with names : " + roleNames));
//		return mapper.toRoleDtoFromRoleSet(roleSet);  //converting roleSet to RoleDto
//	}
//
//	
//	
//	@Override
//	public RoleDto findRoleByName(String name) {
//		Role role = roleRepo.findByName(name)
//						.orElseThrow(()->
//								new RoleNotFoundException("Unable to find Role with name : " + name));
//		return mapper.toRoleDtoFromSingleRole(role); //converting Role to RoleDto
//	}
//
//	
//	
//	@Override
//	public RoleDto findAllRoles() {
//		Set<Role> roleSet = roleRepo.findAllRole()
//								.orElseThrow(()->
//										new RoleNotFoundException("Unable to find any roles in the Database."));
//		return mapper.toRoleDtoFromRoleSet(roleSet); //Converting RoleSet to RoleDto.
//	}
	
	
	
	
	
	
	
	
}
