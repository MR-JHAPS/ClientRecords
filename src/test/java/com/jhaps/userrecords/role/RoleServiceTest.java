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
		
		
		//mock behaviour | act (logic's inside findAllMethods)
		when(roleRepo.findAll()).thenReturn(List.of(role1, role2));
		
		
		//running the roleServiceImpl.findAllRoles(). returns Set<RoleResponse>.
		Set<Role> response = roleServiceImpl.findAllRoles();
		
		//verification of result
		assertEquals(2, response.size());
		assertTrue(response.contains(role1));
		assertTrue(response.contains(role2));
		
		//verifying mock interactions
		verify(roleRepo).findAll();

	}
	
	
	
	
}//ends class
