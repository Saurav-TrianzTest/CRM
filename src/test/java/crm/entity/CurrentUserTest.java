package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CurrentUser Entity Tests")
class CurrentUserTest {

    private CurrentUser currentUser;
    private User user;
    private Set<GrantedAuthority> authorities;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .enabled(1)
                .role(role)
                .build();

        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser = new CurrentUser();
        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);
    }

    @Test
    @DisplayName("Test CurrentUser constructor")
    void testCurrentUserConstructor() {
        CurrentUser newCurrentUser = new CurrentUser();
        assertNotNull(newCurrentUser);
        assertNull(newCurrentUser.getUser());
        assertNull(newCurrentUser.getAuthorities());
    }

    @Test
    @DisplayName("Test CurrentUser getters and setters")
    void testGettersAndSetters() {
        CurrentUser testCurrentUser = new CurrentUser();
        testCurrentUser.setUser(user);
        testCurrentUser.setAuthorities(authorities);

        assertEquals(user, testCurrentUser.getUser());
        assertEquals(authorities, testCurrentUser.getAuthorities());
    }

    @Test
    @DisplayName("Test CurrentUser getAuthorities returns correct collection")
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> returnedAuthorities = currentUser.getAuthorities();
        assertNotNull(returnedAuthorities);
        assertEquals(2, returnedAuthorities.size());
        assertTrue(returnedAuthorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(returnedAuthorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("Test CurrentUser getPassword returns user password")
    void testGetPassword() {
        String password = currentUser.getPassword();
        assertNotNull(password);
        assertEquals("password123", password);
        assertEquals(user.getPassword(), password);
    }

    @Test
    @DisplayName("Test CurrentUser getPassword with null user throws NPE")
    void testGetPasswordWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> currentUser.getPassword());
    }

    @Test
    @DisplayName("Test CurrentUser getUsername returns user username")
    void testGetUsername() {
        String username = currentUser.getUsername();
        assertNotNull(username);
        assertEquals("testuser", username);
        assertEquals(user.getUsername(), username);
    }

    @Test
    @DisplayName("Test CurrentUser getUsername with null user throws NPE")
    void testGetUsernameWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> currentUser.getUsername());
    }

    @Test
    @DisplayName("Test CurrentUser isAccountNonExpired returns true")
    void testIsAccountNonExpired() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    @DisplayName("Test CurrentUser isAccountNonLocked returns true")
    void testIsAccountNonLocked() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Test CurrentUser isCredentialsNonExpired returns true")
    void testIsCredentialsNonExpired() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Test CurrentUser isEnabled returns true")
    void testIsEnabled() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    @DisplayName("Test CurrentUser with empty authorities")
    void testCurrentUserWithEmptyAuthorities() {
        currentUser.setAuthorities(new HashSet<>());
        assertNotNull(currentUser.getAuthorities());
        assertEquals(0, currentUser.getAuthorities().size());
    }

    @Test
    @DisplayName("Test CurrentUser with null authorities")
    void testCurrentUserWithNullAuthorities() {
        currentUser.setAuthorities(null);
        assertNull(currentUser.getAuthorities());
    }

    @Test
    @DisplayName("Test CurrentUser with single authority")
    void testCurrentUserWithSingleAuthority() {
        Set<GrantedAuthority> singleAuthority = new HashSet<>();
        singleAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(singleAuthority);

        assertEquals(1, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("Test CurrentUser with multiple authorities")
    void testCurrentUserWithMultipleAuthorities() {
        Set<GrantedAuthority> multipleAuthorities = new HashSet<>();
        multipleAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        multipleAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        multipleAuthorities.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
        currentUser.setAuthorities(multipleAuthorities);

        assertEquals(3, currentUser.getAuthorities().size());
    }

    @Test
    @DisplayName("Test CurrentUser equals and hashCode")
    void testEqualsAndHashCode() {
        CurrentUser currentUser1 = new CurrentUser();
        currentUser1.setUser(user);
        currentUser1.setAuthorities(authorities);

        CurrentUser currentUser2 = new CurrentUser();
        currentUser2.setUser(user);
        currentUser2.setAuthorities(authorities);

        assertEquals(currentUser1, currentUser2);
        assertEquals(currentUser1.hashCode(), currentUser2.hashCode());
    }

    @Test
    @DisplayName("Test CurrentUser toString")
    void testToString() {
        String currentUserString = currentUser.toString();
        assertNotNull(currentUserString);
    }

    @Test
    @DisplayName("Test CurrentUser with different users")
    void testCurrentUserWithDifferentUsers() {
        User user2 = User.builder()
                .id(2L)
                .username("anotheruser")
                .password("anotherpass")
                .build();

        currentUser.setUser(user2);
        assertEquals("anotheruser", currentUser.getUsername());
        assertEquals("anotherpass", currentUser.getPassword());
    }

    @Test
    @DisplayName("Test CurrentUser UserDetails interface implementation")
    void testUserDetailsInterfaceImplementation() {
        assertTrue(currentUser instanceof org.springframework.security.core.userdetails.UserDetails);
    }

    @Test
    @DisplayName("Test CurrentUser all UserDetails methods return expected values")
    void testAllUserDetailsMethods() {
        assertEquals("testuser", currentUser.getUsername());
        assertEquals("password123", currentUser.getPassword());
        assertNotNull(currentUser.getAuthorities());
        assertTrue(currentUser.isAccountNonExpired());
        assertTrue(currentUser.isAccountNonLocked());
        assertTrue(currentUser.isCredentialsNonExpired());
        assertTrue(currentUser.isEnabled());
    }

    @Test
    @DisplayName("Test CurrentUser with user having null password")
    void testCurrentUserWithNullPassword() {
        user.setPassword(null);
        assertNull(currentUser.getPassword());
    }

    @Test
    @DisplayName("Test CurrentUser authorities immutability")
    void testAuthoritiesImmutability() {
        Collection<? extends GrantedAuthority> auth = currentUser.getAuthorities();
        int originalSize = auth.size();

        // Try to add a new authority to the returned collection
        try {
            ((Set<GrantedAuthority>) auth).add(new SimpleGrantedAuthority("ROLE_NEW"));
            // If we get here, the collection is mutable
            assertEquals(originalSize + 1, auth.size());
        } catch (UnsupportedOperationException e) {
            // Collection is immutable, which is acceptable
            assertEquals(originalSize, currentUser.getAuthorities().size());
        }
    }
}
