package com.jhaps.clientrecords.service.system;

import java.util.Set;

import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.entity.system.Role;

public interface RoleService {

	public boolean isRoleValid(Set<String> roleNames); /* (Multiple Roles at once) - Checking if the  Set<String> roles exists in the RoleNames.enum.  */
	
	public boolean isRoleValid(String roleName); /* (Single Role) - Checking if the role exists in the RoleNames.enum. */
	
	public Role findRoleById(int id);
	
	public Role findRoleByName(String name); //for single role.
	
	public Set<Role> findRoleByNames(Set<String> roleNames); //for multiple role.
	
	public Set<Role> findAllRoles();
	
	public void saveNewRole(RoleDto roleDto);
	
	public void deleteRole(int id);
	
	
}
