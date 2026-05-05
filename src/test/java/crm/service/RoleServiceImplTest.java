package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    private RoleServiceImpl roleService;
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void constructor_shouldCreateRoleServiceImpl() {
        // Assert
        assertNotNull(roleService);
    }

    @Test
    void listAllRoles_shouldReturnAllRoles() {
        // Arrange
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");
        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_USER");
        roles.add(role1);
        roles.add(role2);
        
        when(roleRepository.findAll()).thenReturn(roles);
        
        // Act
        Iterable<Role> result = roleService.listAllRoles();
        
        // Assert
        assertNotNull(result);
        verify(roleRepository).findAll();
    }

    @Test
    void listAllRoles_shouldCallRepository() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        
        // Act
        roleService.listAllRoles();
        
        // Assert
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void service_shouldHaveServiceAnnotation() {
        // Assert
        assertTrue(RoleServiceImpl.class.isAnnotationPresent(
            org.springframework.stereotype.Service.class));
    }
}
