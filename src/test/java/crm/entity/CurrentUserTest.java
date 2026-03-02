package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    private CurrentUser currentUser;
    private User user;
    private Set<GrantedAuthority> authorities;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser();
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");

        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);
    }

    @Test
    void testGetAuthorities() {
        assertEquals(authorities, currentUser.getAuthorities());
        assertNotNull(currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());
    }

    @Test
    void testGetPassword() {
        assertEquals("password123", currentUser.getPassword());
        assertNotNull(currentUser.getPassword());
    }

    @Test
    void testGetUsername() {
        assertEquals("testuser", currentUser.getUsername());
        assertNotNull(currentUser.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpass");

        currentUser.setUser(newUser);
        assertEquals("newuser", currentUser.getUsername());
        assertEquals("newpass", currentUser.getPassword());
    }

    @Test
    void testSetAuthorities() {
        Set<GrantedAuthority> newAuthorities = new HashSet<>();
        newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        currentUser.setAuthorities(newAuthorities);
        assertEquals(1, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testGetPasswordWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> currentUser.getPassword());
    }

    @Test
    void testGetUsernameWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> currentUser.getUsername());
    }
}
