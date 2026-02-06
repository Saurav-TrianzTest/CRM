package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CurrentUserTest {

    private User user;
    private CurrentUser currentUser;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .enabled(1)
                .role(role)
                .build();

        currentUser = new CurrentUser();
        currentUser.setUser(user);
    }

    @Test
    void testDefaultConstructorAndSetter() {
        assertNotNull(currentUser);
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testGetUser() {
        User retrievedUser = currentUser.getUser();

        assertNotNull(retrievedUser);
        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getUsername(), retrievedUser.getUsername());
    }

    @Test
    void testGetId() {
        assertEquals(1L, currentUser.getUser().getId());
    }

    @Test
    void testGetRole() {
        Role role = currentUser.getUser().getRole();

        assertNotNull(role);
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testGetUsername() {
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals("password", currentUser.getPassword());
    }

    @Test
    void testIsEnabled() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    void testIsEnabledWithDisabledUser() {
        user.setEnabled(0);
        CurrentUser disabledUser = new CurrentUser();
        disabledUser.setUser(user);

        assertFalse(disabledUser.isEnabled());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
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
    void testWithAdminRole() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        user.setRole(adminRole);
        CurrentUser adminUser = new CurrentUser();
        adminUser.setUser(user);

        Collection<? extends GrantedAuthority> authorities = adminUser.getAuthorities();

        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testWithNullRole() {
        user.setRole(null);

        assertThrows(NullPointerException.class, () -> {
            CurrentUser testUser = new CurrentUser();
            testUser.setUser(user);
            testUser.getAuthorities();
        });
    }

    @Test
    void testToString() {
        String result = currentUser.toString();

        assertNotNull(result);
    }

    @Test
    void testEquality() {
        CurrentUser currentUser1 = new CurrentUser();
        currentUser1.setUser(user);
        CurrentUser currentUser2 = new CurrentUser();
        currentUser2.setUser(user);

        assertEquals(currentUser1.getUser(), currentUser2.getUser());
    }
}
