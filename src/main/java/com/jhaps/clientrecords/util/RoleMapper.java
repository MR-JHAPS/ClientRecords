package com.jhaps.clientrecords.util;

import org.springframework.stereotype.Component;

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
	

	
}
