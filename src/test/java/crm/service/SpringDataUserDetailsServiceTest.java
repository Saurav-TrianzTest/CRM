package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpringDataUserDetailsServiceTest {

    private SpringDataUserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDetailsService = new SpringDataUserDetailsService();
        userService = mock(UserService.class);
        userDetailsService.userService = userService;
    }

    @Test
    void loadUserByUsername_withValidUsername_shouldReturnUserDetails() {
        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .role(role)
                .build();
        
        when(userService.findByUsername("testuser")).thenReturn(user);
        
        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CurrentUser);
        assertEquals("testuser", result.getUsername());
        verify(userService).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_withNonExistentUsername_shouldThrowException() {
        // Arrange
        when(userService.findByUsername("nonexistent")).thenReturn(null);
        
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
        verify(userService).findByUsername("nonexistent");
    }

    @Test
    void loadUserByUsername_shouldSetAuthorities() {
        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");
        
        User user = User.builder()
                .id(1L)
                .username("admin")
                .password("password")
                .role(role)
                .build();
        
        when(userService.findByUsername("admin")).thenReturn(user);
        
        // Act
        UserDetails result = userDetailsService.loadUserByUsername("admin");
        
        // Assert
        assertNotNull(result.getAuthorities());
        assertFalse(result.getAuthorities().isEmpty());
    }

    @Test
    void service_shouldHaveServiceAnnotation() {
        // Assert
        assertTrue(SpringDataUserDetailsService.class.isAnnotationPresent(
            org.springframework.stereotype.Service.class));
    }
}
