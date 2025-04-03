package com.jhaps.userrecords.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jhaps.clientrecords.dto.response.RoleResponse;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.repository.system.RoleRepository;
import com.jhaps.clientrecords.serviceImpl.system.RoleServiceImpl;
import com.jhaps.clientrecords.util.mapper.RoleMapper;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

	@Mock
	private RoleRepository roleRepo;
	@Mock
	private RoleMapper roleMapper;
	@InjectMocks
	private RoleServiceImpl roleServiceImpl;
	
	
	@Test
	void findAllRoles_WhenRolesExists_ReturnSetOfRoleResponse() {
		//setting up role test data. | arrange
		Role role1 = new Role(1, "user");
		Role role2 = new Role(2, "admin");
		RoleResponse roleResponse1 = new RoleResponse(1, "user");
		RoleResponse roleResponse2 = new RoleResponse(2, "admin");
		
		//mock behaviour | act (logic's inside findAllMethods)
		when(roleRepo.findAll()).thenReturn(List.of(role1, role2));
		when(roleMapper.toRoleResponse(role1)).thenReturn(roleResponse1);
		when(roleMapper.toRoleResponse(role2)).thenReturn(roleResponse2);
		
		//running the roleServiceImpl.findAllRoles(). returns Set<RoleResponse>.
		Set<RoleResponse> response = roleServiceImpl.findAllRoles();
		
		//verification of result
		assertEquals(2, response.size());
		assertTrue(response.contains(roleResponse1));
		assertTrue(response.contains(roleResponse2));
		
		//verifying mock interactions
		verify(roleRepo).findAll();
		verify(roleMapper).toRoleResponse(role1);

	}
	
	
	
	
}//ends class
