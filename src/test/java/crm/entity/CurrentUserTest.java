package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CurrentUserTest {

    private CurrentUser currentUser;

    @BeforeEach
    public void setUp() {
        currentUser = new CurrentUser();
    }

    @Test
    public void testCurrentUserCreation() {
        assertNotNull(currentUser);
    }

    @Test
    public void testSetAndGetUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        currentUser.setUser(user);

        assertNotNull(currentUser.getUser());
        assertEquals("testuser", currentUser.getUser().getUsername());
    }

    @Test
    public void testSetAndGetAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);

        assertNotNull(currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());
    }

    @Test
    public void testGetPassword() {
        User user = new User();
        user.setPassword("securePass123");
        currentUser.setUser(user);

        assertEquals("securePass123", currentUser.getPassword());
    }

    @Test
    public void testGetUsername() {
        User user = new User();
        user.setUsername("johndoe");
        currentUser.setUser(user);

        assertEquals("johndoe", currentUser.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    public void testGetAuthoritiesReturnsCorrectValue() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);

        assertEquals(2, currentUser.getAuthorities().size());
    }

    @Test
    public void testCurrentUserWithNullUser() {
        currentUser.setUser(null);
        assertNull(currentUser.getUser());
    }

    @Test
    public void testCurrentUserWithEmptyAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        currentUser.setAuthorities(authorities);

        assertNotNull(currentUser.getAuthorities());
        assertTrue(currentUser.getAuthorities().isEmpty());
    }
}
