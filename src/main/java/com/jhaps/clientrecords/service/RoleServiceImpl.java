package com.jhaps.clientrecords.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.RoleDto;
import com.jhaps.clientrecords.entity.Role;
import com.jhaps.clientrecords.exception.RoleNotFoundException;
import com.jhaps.clientrecords.repository.RoleRepository;
import com.jhaps.clientrecords.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

	private RoleRepository roleRepo;
	
	private Mapper mapper;

	public RoleServiceImpl(RoleRepository roleRepo, Mapper mapper) {
		this.roleRepo = roleRepo;
		this.mapper = mapper;
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

	
	
	@Override
	public Set<Role> findAllRoles() {
		log.info("Getting the List of all the Roles.");
		List<Role> roleList = roleRepo.findAll();
		if(roleList.isEmpty()) {
			throw new RoleNotFoundException("Unable to find any roles in the Database.");
		}
		Set<Role> roleSet = roleList.stream().collect(Collectors.toSet());  //converting roleList to roleSet.
		return roleSet; 
	}




	@Override
	public void saveNewRole(RoleDto roleDto) {
		String roleName = roleDto.getRoleNames().toString(); //converting Set<String> to String
		
		/*The converted String still contains "[" and "]" brackets like this [role]"
		 *we will remove the brackets from string. 
		 */
		 String formattedRoleName = roleName.substring(1, roleName.length()-1); //removed the brackets
		 Role role = new Role(0, formattedRoleName);	//saving new role.
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
