package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SpringDataUserDetailsService springDataUserDetailsService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

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
    @DisplayName("Test findByUsername returns correct user")
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User found = userService.findByUsername("testuser");

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Test listAllUsers returns only enabled users")
    void testListAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAllByEnabled(1)).thenReturn(users);

        Iterable<User> result = userService.listAllUsers();

        assertNotNull(result);
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    @DisplayName("Test showUser returns user by id")
    void testShowUser() {
        when(userRepository.findById(1L).orElse(null)).thenReturn(user);

        User found = userService.showUser(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userRepository, times(1)).findById(1L).orElse(null);
    }

    @Test
    @DisplayName("Test saveUser encodes password and sets role")
    void testSaveUser() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));

        userService.saveUser(user);

        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    @DisplayName("Test saveUser sets user to enabled")
    void testSaveUserSetsEnabled() {
        User newUser = User.builder()
                .username("newuser")
                .password("pass")
                .enabled(0)
                .build();

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));

        userService.saveUser(newUser);

        assertEquals(1, newUser.getEnabled());
    }

    @Test
    @DisplayName("Test editUser updates user with encoded password")
    void testEditUser() {
        when(passwordEncoder.encode("newpassword")).thenReturn("encoded_new_password");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(roleRepository.findById(1).orElse(null)).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        user.setPassword("newpassword");
        userService.editUser(user);

        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test editUser handles null role gracefully")
    void testEditUserWithNullRole() {
        User userWithoutRole = User.builder()
                .id(2L)
                .username("noroleuser")
                .password("pass")
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(userWithoutRole);

        userService.editUser(userWithoutRole);

        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(userWithoutRole);
    }

    @Test
    @DisplayName("Test deleteUser sets enabled to 0 and password to null")
    void testDeleteUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.deleteUser(user);

        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test deleteUser does not actually delete from database")
    void testDeleteUserSoftDelete() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.deleteUser(user);

        verify(userRepository, never()).delete(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test findByUsername returns null when user not found")
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("notfound")).thenReturn(null);

        User found = userService.findByUsername("notfound");

        assertNull(found);
    }

    @Test
    @DisplayName("Test showUser returns null when user not found")
    void testShowUserNotFound() {
        when(userRepository.findById(999L).orElse(null)).thenReturn(null);

        User found = userService.showUser(999L);

        assertNull(found);
    }
}
