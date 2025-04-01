package com.jhaps.clientrecords.service.system;

import java.util.List;
import java.util.Set;

import com.jhaps.clientrecords.dto.request.RoleSaveRequest;
import com.jhaps.clientrecords.dto.request.RoleRequest;
import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.dto.response.RoleResponse;
import com.jhaps.clientrecords.entity.system.Role;

public interface RoleService {

	public boolean isRoleValid(Set<String> roleNames); /* (Multiple Roles at once) - Checking if the  Set<String> roles exists in the RoleNames.enum.  */
	
	public boolean isRoleValid(String roleName); /* (Single Role) - Checking if the role exists in the RoleNames.enum. */
	
	public Role findRoleById(int id);
	
	public Role findRoleByName(String name); //for single role.
	
	public Set<Role> findRoleByNames(Set<String> roleNames); //for multiple role.
	
	public Set<RoleResponse> findAllRoles();
	
	public void saveNewRole(RoleSaveRequest roleSaveRequest);
	
	public void deleteRole(int id);
	
	
}
