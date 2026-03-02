package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpringDataUserDetailsServiceTest {

    private SpringDataUserDetailsService service;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new SpringDataUserDetailsService();
        service.userService = userService;
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRole(role);

        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails result = service.loadUserByUsername("testuser");

        assertNotNull(result);
        assertTrue(result instanceof CurrentUser);
        assertEquals("testuser", result.getUsername());
        verify(userService).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void testLoadUserByUsernameWithAdminRole() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("adminpass");

        Role role = new Role();
        role.setName("ROLE_ADMIN");
        user.setRole(role);

        when(userService.findByUsername("admin")).thenReturn(user);

        UserDetails result = service.loadUserByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
    }
}
