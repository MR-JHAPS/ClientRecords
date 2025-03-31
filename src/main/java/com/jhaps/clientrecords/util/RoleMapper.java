package com.jhaps.clientrecords.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.dto.response.RoleResponse;
import com.jhaps.clientrecords.entity.system.Role;

@Component
public class RoleMapper {

	//converting Set<Role> to Set<RoleResponse>
	
	public RoleResponse toRoleResponse(Role role) {
		RoleResponse roleResponse = new RoleResponse();
		roleResponse.setId(role.getId());
		roleResponse.setRole(role.getName());
		return roleResponse;
	}
	
	
	
	
	
	
	
	
	
/*-----------------------RoleDto-Set<Role> Not Role Entity---------------------------------------------------------------------------------------------------------------*/
	
	//Converting Set<Roles> to RoleDto.   **RoleDto-->RoleNames is of type Set<String>
	public RoleDto toRoleDtoFromRoleSet(Set<Role> roleList) {
		RoleDto dto = new RoleDto();
		//collecting/extracting roles of Set<Role> roleList to Set<String>
		Set<String> roles  = roleList.stream()
								.map(role -> role.getName())
								.collect(Collectors.toSet());
		dto.setRoleNames(roles);
		return dto;
	}
	
	
	
	//Converting Single Role to RoleDto
	public RoleDto toRoleDtoFromSingleRole(Role role) {
		RoleDto dto = new RoleDto();
		Set<String> roleSet = new HashSet<>();
		
		roleSet.add(role.getName());
		dto.setRoleNames(roleSet);
		return dto;
	}
	
	
	
	
	
}
