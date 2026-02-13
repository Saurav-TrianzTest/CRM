package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for SpringDataUserDetailsService
 * Tests all public methods, constructors, edge cases, null checks, and boundary conditions
 */
@ExtendWith(MockitoExtension.class)
class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService userDetailsService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("encodedPassword")
                .enabled(1)
                .role(testRole)
                .build();
    }

    // loadUserByUsername Tests
    @Test
    void testLoadUserByUsername_WithExistingUser() {
        // Arrange
        String username = "testuser";
        when(userService.findByUsername(username)).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CurrentUser);
        assertEquals(username, result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(testUser, currentUser.getUser());

        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_WithNonExistingUser() {
        // Arrange
        String username = "nonexisting";
        when(userService.findByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals(username, exception.getMessage());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_WithNullUsername() {
        // Arrange
        when(userService.findByUsername(null)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });

        assertNull(exception.getMessage());
        verify(userService, times(1)).findByUsername(null);
    }

    @Test
    void testLoadUserByUsername_WithEmptyUsername() {
        // Arrange
        String username = "";
        when(userService.findByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals(username, exception.getMessage());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_WithWhitespaceUsername() {
        // Arrange
        String username = "   ";
        when(userService.findByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals(username, exception.getMessage());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_WithAdminRole() {
        // Arrange
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        User adminUser = User.builder()
                .id(2L)
                .username("adminuser")
                .email("admin@example.com")
                .password("adminPassword")
                .enabled(1)
                .role(adminRole)
                .build();

        when(userService.findByUsername("adminuser")).thenReturn(adminUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("adminuser");

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CurrentUser);
        assertEquals("adminuser", result.getUsername());

        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        verify(userService, times(1)).findByUsername("adminuser");
    }

    @Test
    void testLoadUserByUsername_WithSpecialCharactersInUsername() {
        // Arrange
        String username = "user@test#2024";
        User specialUser = User.builder()
                .id(3L)
                .username(username)
                .email("special@test.com")
                .password("password")
                .enabled(1)
                .role(testRole)
                .build();

        when(userService.findByUsername(username)).thenReturn(specialUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_WithDisabledUser() {
        // Arrange
        User disabledUser = User.builder()
                .id(4L)
                .username("disableduser")
                .email("disabled@test.com")
                .password("password")
                .enabled(0)
                .role(testRole)
                .build();

        when(userService.findByUsername("disableduser")).thenReturn(disabledUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("disableduser");

        // Assert
        assertNotNull(result);
        assertEquals("disableduser", result.getUsername());
        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(0, currentUser.getUser().getEnabled());
        verify(userService, times(1)).findByUsername("disableduser");
    }

    @Test
    void testLoadUserByUsername_VerifyCurrentUserProperties() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        CurrentUser currentUser = (CurrentUser) result;

        assertEquals(testUser, currentUser.getUser());
        assertEquals(testUser.getUsername(), currentUser.getUsername());
        assertEquals(testUser.getPassword(), currentUser.getPassword());

        assertNotNull(currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());

        assertTrue(currentUser.isAccountNonExpired());
        assertTrue(currentUser.isAccountNonLocked());
        assertTrue(currentUser.isCredentialsNonExpired());
        assertTrue(currentUser.isEnabled());

        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_VerifyGrantedAuthoritiesCreation() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        GrantedAuthority authority = authorities.iterator().next();
        assertTrue(authority instanceof SimpleGrantedAuthority);
        assertEquals("ROLE_USER", authority.getAuthority());

        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_WithUserWithoutPassword() {
        // Arrange
        User userWithoutPassword = User.builder()
                .id(5L)
                .username("nopassuser")
                .email("nopass@test.com")
                .password(null)
                .enabled(1)
                .role(testRole)
                .build();

        when(userService.findByUsername("nopassuser")).thenReturn(userWithoutPassword);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("nopassuser");

        // Assert
        assertNotNull(result);
        assertNull(result.getPassword());
        verify(userService, times(1)).findByUsername("nopassuser");
    }

    @Test
    void testLoadUserByUsername_WithUserWithEmptyPassword() {
        // Arrange
        User userWithEmptyPassword = User.builder()
                .id(6L)
                .username("emptypassuser")
                .email("emptypass@test.com")
                .password("")
                .enabled(1)
                .role(testRole)
                .build();

        when(userService.findByUsername("emptypassuser")).thenReturn(userWithEmptyPassword);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("emptypassuser");

        // Assert
        assertNotNull(result);
        assertEquals("", result.getPassword());
        verify(userService, times(1)).findByUsername("emptypassuser");
    }

    @Test
    void testLoadUserByUsername_WithUserWithNullRole() {
        // Arrange
        User userWithNullRole = User.builder()
                .id(7L)
                .username("nullroleuser")
                .email("nullrole@test.com")
                .password("password")
                .enabled(1)
                .role(null)
                .build();

        when(userService.findByUsername("nullroleuser")).thenReturn(userWithNullRole);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            userDetailsService.loadUserByUsername("nullroleuser");
        });

        verify(userService, times(1)).findByUsername("nullroleuser");
    }

    @Test
    void testLoadUserByUsername_WithRoleWithNullName() {
        // Arrange
        Role roleWithNullName = new Role();
        roleWithNullName.setId(10);
        roleWithNullName.setName(null);

        User userWithNullRoleName = User.builder()
                .id(8L)
                .username("nullrolenameuser")
                .email("nullrolename@test.com")
                .password("password")
                .enabled(1)
                .role(roleWithNullName)
                .build();

        when(userService.findByUsername("nullrolenameuser")).thenReturn(userWithNullRoleName);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("nullrolenameuser");

        // Assert
        assertNotNull(result);
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        GrantedAuthority authority = authorities.iterator().next();
        assertNull(authority.getAuthority());

        verify(userService, times(1)).findByUsername("nullrolenameuser");
    }

    @Test
    void testLoadUserByUsername_WithRoleWithEmptyName() {
        // Arrange
        Role roleWithEmptyName = new Role();
        roleWithEmptyName.setId(11);
        roleWithEmptyName.setName("");

        User userWithEmptyRoleName = User.builder()
                .id(9L)
                .username("emptyrolenameuser")
                .email("emptyrolename@test.com")
                .password("password")
                .enabled(1)
                .role(roleWithEmptyName)
                .build();

        when(userService.findByUsername("emptyrolenameuser")).thenReturn(userWithEmptyRoleName);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("emptyrolenameuser");

        // Assert
        assertNotNull(result);
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        GrantedAuthority authority = authorities.iterator().next();
        assertEquals("", authority.getAuthority());

        verify(userService, times(1)).findByUsername("emptyrolenameuser");
    }

    @Test
    void testLoadUserByUsername_WithModeratorRole() {
        // Arrange
        Role moderatorRole = new Role();
        moderatorRole.setId(12);
        moderatorRole.setName("ROLE_MODERATOR");

        User moderatorUser = User.builder()
                .id(10L)
                .username("moderator")
                .email("moderator@test.com")
                .password("modPassword")
                .enabled(1)
                .role(moderatorRole)
                .build();

        when(userService.findByUsername("moderator")).thenReturn(moderatorUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("moderator");

        // Assert
        assertNotNull(result);
        assertEquals("moderator", result.getUsername());

        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MODERATOR")));

        verify(userService, times(1)).findByUsername("moderator");
    }

    @Test
    void testLoadUserByUsername_MultipleCalls() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result1 = userDetailsService.loadUserByUsername("testuser");
        UserDetails result2 = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getUsername(), result2.getUsername());
        verify(userService, times(2)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_VerifyUserServiceCalledOnce() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        userDetailsService.loadUserByUsername("testuser");

        // Assert
        verify(userService, times(1)).findByUsername("testuser");
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testLoadUserByUsername_WithLongUsername() {
        // Arrange
        String longUsername = "a".repeat(255);
        User userWithLongUsername = User.builder()
                .id(11L)
                .username(longUsername)
                .email("long@test.com")
                .password("password")
                .enabled(1)
                .role(testRole)
                .build();

        when(userService.findByUsername(longUsername)).thenReturn(userWithLongUsername);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(longUsername);

        // Assert
        assertNotNull(result);
        assertEquals(longUsername, result.getUsername());
        verify(userService, times(1)).findByUsername(longUsername);
    }

    @Test
    void testLoadUserByUsername_CaseInsensitiveSearch() {
        // Arrange
        String mixedCaseUsername = "TestUser";
        User mixedCaseUser = User.builder()
                .id(12L)
                .username(mixedCaseUsername)
                .email("mixed@test.com")
                .password("password")
                .enabled(1)
                .role(testRole)
                .build();

        when(userService.findByUsername(mixedCaseUsername)).thenReturn(mixedCaseUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(mixedCaseUsername);

        // Assert
        assertNotNull(result);
        assertEquals(mixedCaseUsername, result.getUsername());
        verify(userService, times(1)).findByUsername(mixedCaseUsername);
    }

    @Test
    void testLoadUserByUsername_WithRoleContainingSpecialCharacters() {
        // Arrange
        Role specialRole = new Role();
        specialRole.setId(13);
        specialRole.setName("ROLE_ADMIN@2024");

        User userWithSpecialRole = User.builder()
                .id(13L)
                .username("specialroleuser")
                .email("specialrole@test.com")
                .password("password")
                .enabled(1)
                .role(specialRole)
                .build();

        when(userService.findByUsername("specialroleuser")).thenReturn(userWithSpecialRole);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("specialroleuser");

        // Assert
        assertNotNull(result);
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN@2024")));

        verify(userService, times(1)).findByUsername("specialroleuser");
    }

    @Test
    void testLoadUserByUsername_VerifyCurrentUserIsNotNull() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CurrentUser);
        CurrentUser currentUser = (CurrentUser) result;
        assertNotNull(currentUser.getUser());
        assertNotNull(currentUser.getAuthorities());
        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_WithDifferentUsers() {
        // Arrange
        User user1 = User.builder()
                .id(14L)
                .username("user1")
                .email("user1@test.com")
                .password("pass1")
                .enabled(1)
                .role(testRole)
                .build();

        User user2 = User.builder()
                .id(15L)
                .username("user2")
                .email("user2@test.com")
                .password("pass2")
                .enabled(1)
                .role(testRole)
                .build();

        when(userService.findByUsername("user1")).thenReturn(user1);
        when(userService.findByUsername("user2")).thenReturn(user2);

        // Act
        UserDetails result1 = userDetailsService.loadUserByUsername("user1");
        UserDetails result2 = userDetailsService.loadUserByUsername("user2");

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getUsername(), result2.getUsername());
        assertEquals("user1", result1.getUsername());
        assertEquals("user2", result2.getUsername());
        verify(userService, times(1)).findByUsername("user1");
        verify(userService, times(1)).findByUsername("user2");
    }
}
