package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void testConstructor() {
        assertNotNull(roleService);
    }

    @Test
    void testListAllRoles() {
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");
        roles.add(role1);

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");
        roles.add(role2);

        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
        assertEquals(roles, result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRolesEmpty() {
        List<Role> roles = new ArrayList<>();
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRolesCallsRepository() {
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        roleService.listAllRoles();
        verify(roleRepository).findAll();
    }
}
