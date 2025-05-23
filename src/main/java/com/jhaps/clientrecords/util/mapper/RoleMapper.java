package com.jhaps.clientrecords.util.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
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
	

	
	// Converting to Set<String> of roles. This is to add to jwt Token for front-end.
	public Set<String> roleToStringSet(Collection<? extends GrantedAuthority> roles){
		Set<String> roleSet = roles.stream().map(role-> role.toString()).collect(Collectors.toSet());
		return roleSet;
	}
	
	
}
