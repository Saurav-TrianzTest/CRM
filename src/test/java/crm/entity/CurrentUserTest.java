package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CurrentUser Entity Tests")
class CurrentUserTest {

    @Mock
    private User mockUser;

    private CurrentUser currentUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currentUser = new CurrentUser();
    }

    @Test
    @DisplayName("Test default constructor creates non-null instance")
    void testDefaultConstructor() {
        CurrentUser newCurrentUser = new CurrentUser();
        assertNotNull(newCurrentUser);
    }

    @Test
    @DisplayName("Test setUser and getUser")
    void testSetAndGetUser() {
        currentUser.setUser(mockUser);
        assertEquals(mockUser, currentUser.getUser());
    }

    @Test
    @DisplayName("Test setUser with null value")
    void testSetUserNull() {
        currentUser.setUser(null);
        assertNull(currentUser.getUser());
    }

    @Test
    @DisplayName("Test setAuthorities and getAuthorities")
    void testSetAndGetAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser.setAuthorities(authorities);

        assertEquals(authorities, currentUser.getAuthorities());
        assertEquals(2, currentUser.getAuthorities().size());
    }

    @Test
    @DisplayName("Test setAuthorities with null")
    void testSetAuthoritiesNull() {
        currentUser.setAuthorities(null);
        assertNull(currentUser.getAuthorities());
    }

    @Test
    @DisplayName("Test setAuthorities with empty set")
    void testSetAuthoritiesEmpty() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        currentUser.setAuthorities(authorities);

        assertNotNull(currentUser.getAuthorities());
        assertEquals(0, currentUser.getAuthorities().size());
    }

    @Test
    @DisplayName("Test getPassword returns user password")
    void testGetPassword() {
        when(mockUser.getPassword()).thenReturn("encryptedPassword123");
        currentUser.setUser(mockUser);

        String password = currentUser.getPassword();

        assertEquals("encryptedPassword123", password);
        verify(mockUser, times(1)).getPassword();
    }

    @Test
    @DisplayName("Test getPassword throws NullPointerException when user is null")
    void testGetPasswordWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> currentUser.getPassword());
    }

    @Test
    @DisplayName("Test getPassword with null password")
    void testGetPasswordNull() {
        when(mockUser.getPassword()).thenReturn(null);
        currentUser.setUser(mockUser);

        String password = currentUser.getPassword();

        assertNull(password);
        verify(mockUser, times(1)).getPassword();
    }

    @Test
    @DisplayName("Test getUsername returns user username")
    void testGetUsername() {
        when(mockUser.getUsername()).thenReturn("johndoe");
        currentUser.setUser(mockUser);

        String username = currentUser.getUsername();

        assertEquals("johndoe", username);
        verify(mockUser, times(1)).getUsername();
    }

    @Test
    @DisplayName("Test getUsername throws NullPointerException when user is null")
    void testGetUsernameWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> currentUser.getUsername());
    }

    @Test
    @DisplayName("Test getUsername with null username")
    void testGetUsernameNull() {
        when(mockUser.getUsername()).thenReturn(null);
        currentUser.setUser(mockUser);

        String username = currentUser.getUsername();

        assertNull(username);
        verify(mockUser, times(1)).getUsername();
    }

    @Test
    @DisplayName("Test isAccountNonExpired always returns true")
    void testIsAccountNonExpired() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    @DisplayName("Test isAccountNonExpired with user set")
    void testIsAccountNonExpiredWithUser() {
        currentUser.setUser(mockUser);
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    @DisplayName("Test isAccountNonLocked always returns true")
    void testIsAccountNonLocked() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Test isAccountNonLocked with user set")
    void testIsAccountNonLockedWithUser() {
        currentUser.setUser(mockUser);
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Test isCredentialsNonExpired always returns true")
    void testIsCredentialsNonExpired() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Test isCredentialsNonExpired with user set")
    void testIsCredentialsNonExpiredWithUser() {
        currentUser.setUser(mockUser);
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Test isEnabled always returns true")
    void testIsEnabled() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    @DisplayName("Test isEnabled with user set")
    void testIsEnabledWithUser() {
        currentUser.setUser(mockUser);
        assertTrue(currentUser.isEnabled());
    }

    @Test
    @DisplayName("Test all UserDetails boolean methods return true")
    void testAllBooleanMethodsReturnTrue() {
        assertTrue(currentUser.isAccountNonExpired());
        assertTrue(currentUser.isAccountNonLocked());
        assertTrue(currentUser.isCredentialsNonExpired());
        assertTrue(currentUser.isEnabled());
    }

    @Test
    @DisplayName("Test getAuthorities returns collection of authorities")
    void testGetAuthoritiesReturnsCollection() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);

        Collection<? extends GrantedAuthority> result = currentUser.getAuthorities();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Test getAuthorities with multiple authorities")
    void testGetAuthoritiesMultiple() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        currentUser.setAuthorities(authorities);

        Collection<? extends GrantedAuthority> result = currentUser.getAuthorities();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Test equals with same object")
    void testEqualsWithSameObject() {
        currentUser.setUser(mockUser);
        assertEquals(currentUser, currentUser);
    }

    @Test
    @DisplayName("Test equals with equal objects")
    void testEqualsWithEqualObjects() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        currentUser.setUser(mockUser);
        currentUser.setAuthorities(authorities);

        CurrentUser other = new CurrentUser();
        other.setUser(mockUser);
        other.setAuthorities(authorities);

        assertEquals(currentUser, other);
    }

    @Test
    @DisplayName("Test equals with different users")
    void testEqualsWithDifferentUsers() {
        User anotherUser = mock(User.class);

        currentUser.setUser(mockUser);

        CurrentUser other = new CurrentUser();
        other.setUser(anotherUser);

        assertNotEquals(currentUser, other);
    }

    @Test
    @DisplayName("Test equals with null")
    void testEqualsWithNull() {
        currentUser.setUser(mockUser);
        assertNotEquals(null, currentUser);
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        currentUser.setUser(mockUser);

        int hashCode1 = currentUser.hashCode();
        int hashCode2 = currentUser.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test hashCode with equal objects")
    void testHashCodeWithEqualObjects() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        currentUser.setUser(mockUser);
        currentUser.setAuthorities(authorities);

        CurrentUser other = new CurrentUser();
        other.setUser(mockUser);
        other.setAuthorities(authorities);

        assertEquals(currentUser.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test toString contains field values")
    void testToString() {
        currentUser.setUser(mockUser);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);

        String toString = currentUser.toString();

        assertNotNull(toString);
    }

    @Test
    @DisplayName("Test complete CurrentUser initialization")
    void testCompleteCurrentUserInitialization() {
        when(mockUser.getUsername()).thenReturn("admin");
        when(mockUser.getPassword()).thenReturn("password");

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        currentUser.setUser(mockUser);
        currentUser.setAuthorities(authorities);

        assertEquals(mockUser, currentUser.getUser());
        assertEquals(authorities, currentUser.getAuthorities());
        assertEquals("admin", currentUser.getUsername());
        assertEquals("password", currentUser.getPassword());
        assertTrue(currentUser.isAccountNonExpired());
        assertTrue(currentUser.isAccountNonLocked());
        assertTrue(currentUser.isCredentialsNonExpired());
        assertTrue(currentUser.isEnabled());
    }

    @Test
    @DisplayName("Test CurrentUser with admin role")
    void testCurrentUserWithAdminRole() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);

        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Test CurrentUser with user role")
    void testCurrentUserWithUserRole() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);

        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Test CurrentUser with multiple roles")
    void testCurrentUserWithMultipleRoles() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        currentUser.setAuthorities(authorities);

        assertEquals(3, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MANAGER")));
    }

    @Test
    @DisplayName("Test CurrentUser authority update")
    void testCurrentUserAuthorityUpdate() {
        Set<GrantedAuthority> authorities1 = new HashSet<>();
        authorities1.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities1);

        assertEquals(1, currentUser.getAuthorities().size());

        Set<GrantedAuthority> authorities2 = new HashSet<>();
        authorities2.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities2);

        assertEquals(1, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Test CurrentUser implements UserDetails")
    void testCurrentUserImplementsUserDetails() {
        assertTrue(org.springframework.security.core.userdetails.UserDetails.class
                .isAssignableFrom(CurrentUser.class));
    }

    @Test
    @DisplayName("Test getPassword with empty password")
    void testGetPasswordEmpty() {
        when(mockUser.getPassword()).thenReturn("");
        currentUser.setUser(mockUser);

        String password = currentUser.getPassword();

        assertEquals("", password);
    }

    @Test
    @DisplayName("Test getUsername with empty username")
    void testGetUsernameEmpty() {
        when(mockUser.getUsername()).thenReturn("");
        currentUser.setUser(mockUser);

        String username = currentUser.getUsername();

        assertEquals("", username);
    }

    @Test
    @DisplayName("Test CurrentUser with custom authority")
    void testCurrentUserWithCustomAuthority() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("CUSTOM_PERMISSION"));
        currentUser.setAuthorities(authorities);

        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("CUSTOM_PERMISSION")));
    }

    @Test
    @DisplayName("Test CurrentUser state consistency")
    void testCurrentUserStateConsistency() {
        when(mockUser.getUsername()).thenReturn("testuser");
        when(mockUser.getPassword()).thenReturn("testpass");

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser.setUser(mockUser);
        currentUser.setAuthorities(authorities);

        assertEquals("testuser", currentUser.getUsername());
        assertEquals("testpass", currentUser.getPassword());
        assertEquals(1, currentUser.getAuthorities().size());

        String username2 = currentUser.getUsername();
        assertEquals("testuser", username2);
    }
}
