package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService userDetailsService;

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    public void testLoadUserByUsername() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    public void testLoadUserByUsernameReturnsCurrentUser() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("testuser");
        assertTrue(result instanceof CurrentUser);
    }

    @Test
    public void testLoadUserByUsernameHasAuthorities() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("testuser");
        assertNotNull(result.getAuthorities());
        assertFalse(result.getAuthorities().isEmpty());
    }

    @Test
    public void testLoadUserByUsernameWithAdminRole() {
        role.setName("ROLE_ADMIN");
        user.setRole(role);
        when(userService.findByUsername("adminuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("adminuser");
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }
}
