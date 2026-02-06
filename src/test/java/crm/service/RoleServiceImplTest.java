package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAllRoles() {
        ArrayList<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        roles.add(role);

        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleServiceImpl.listAllRoles();
        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRolesReturnsEmpty() {
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        Iterable<Role> result = roleServiceImpl.listAllRoles();
        assertNotNull(result);
    }

    @Test
    void testConstructor() {
        assertNotNull(roleServiceImpl);
    }
}
