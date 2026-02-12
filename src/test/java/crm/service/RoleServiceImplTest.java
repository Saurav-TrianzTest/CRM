package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoleServiceImpl Tests")
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Test listAllRoles returns all roles")
    void testListAllRoles() {
        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_USER");

        List<Role> roles = Arrays.asList(role, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test listAllRoles returns empty list when no roles")
    void testListAllRolesEmpty() {
        List<Role> roles = Arrays.asList();
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test listAllRoles calls repository once")
    void testListAllRolesCallsRepositoryOnce() {
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(roles);

        roleService.listAllRoles();

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test listAllRoles returns multiple roles")
    void testListAllRolesMultiple() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_USER");

        Role role3 = new Role();
        role3.setId(3);
        role3.setName("ROLE_MODERATOR");

        List<Role> roles = Arrays.asList(role1, role2, role3);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
        List<Role> resultList = (List<Role>) result;
        assertEquals(3, resultList.size());
    }

    @Test
    @DisplayName("Test listAllRoles never returns null")
    void testListAllRolesNeverNull() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList());

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
    }

    @Test
    @DisplayName("Test listAllRoles returns correct role names")
    void testListAllRolesCorrectNames() {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        List<Role> roles = Arrays.asList(adminRole, userRole);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();
        List<Role> resultList = (List<Role>) result;

        assertTrue(resultList.stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName())));
        assertTrue(resultList.stream().anyMatch(r -> "ROLE_USER".equals(r.getName())));
    }

    @Test
    @DisplayName("Test listAllRoles with single role")
    void testListAllRolesSingle() {
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();
        List<Role> resultList = (List<Role>) result;

        assertEquals(1, resultList.size());
        assertEquals("ROLE_ADMIN", resultList.get(0).getName());
    }

    @Test
    @DisplayName("Test listAllRoles returns iterable")
    void testListAllRolesReturnsIterable() {
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        assertTrue(result instanceof Iterable);
    }

    @Test
    @DisplayName("Test listAllRoles can be iterated")
    void testListAllRolesCanBeIterated() {
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        int count = 0;
        for (Role r : result) {
            count++;
        }

        assertEquals(1, count);
    }
}
