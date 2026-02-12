package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringDataUserDetailsService Tests")
class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService userDetailsService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    @DisplayName("Test loadUserByUsername returns CurrentUser with valid username")
    void testLoadUserByUsernameSuccess() {
        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CurrentUser);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());

        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Test loadUserByUsername throws exception for null user")
    void testLoadUserByUsernameUserNotFound() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        verify(userService, times(1)).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Test loadUserByUsername sets correct authorities")
    void testLoadUserByUsernameAuthorities() {
        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());

        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }

    @Test
    @DisplayName("Test loadUserByUsername with USER role")
    void testLoadUserByUsernameWithUserRole() {
        Role userRole = new Role();
        userRole.setId(2);
        userRole.setName("ROLE_USER");
        user.setRole(userRole);

        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    @DisplayName("Test loadUserByUsername returns CurrentUser with correct user")
    void testLoadUserByUsernameCurrentUserHasCorrectUser() {
        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        CurrentUser currentUser = (CurrentUser) userDetails;

        assertNotNull(currentUser.getUser());
        assertEquals(user, currentUser.getUser());
    }

    @Test
    @DisplayName("Test loadUserByUsername with empty username throws exception")
    void testLoadUserByUsernameEmptyUsername() {
        when(userService.findByUsername("")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });
    }

    @Test
    @DisplayName("Test loadUserByUsername exception message contains username")
    void testLoadUserByUsernameExceptionMessage() {
        String username = "nonexistent";
        when(userService.findByUsername(username)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertTrue(exception.getMessage().contains(username));
    }

    @Test
    @DisplayName("Test loadUserByUsername creates new CurrentUser instance")
    void testLoadUserByUsernameCreatesNewInstance() {
        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails1 = userDetailsService.loadUserByUsername("testuser");
        UserDetails userDetails2 = userDetailsService.loadUserByUsername("testuser");

        assertNotSame(userDetails1, userDetails2);
    }

    @Test
    @DisplayName("Test loadUserByUsername with different usernames")
    void testLoadUserByUsernameMultipleUsers() {
        User user2 = User.builder()
                .id(2L)
                .username("anotheruser")
                .password("pass456")
                .role(role)
                .build();

        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.findByUsername("anotheruser")).thenReturn(user2);

        UserDetails userDetails1 = userDetailsService.loadUserByUsername("testuser");
        UserDetails userDetails2 = userDetailsService.loadUserByUsername("anotheruser");

        assertEquals("testuser", userDetails1.getUsername());
        assertEquals("anotheruser", userDetails2.getUsername());
    }

    @Test
    @DisplayName("Test loadUserByUsername UserDetails implementation")
    void testLoadUserByUsernameReturnsUserDetailsInterface() {
        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails.getUsername());
        assertNotNull(userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
