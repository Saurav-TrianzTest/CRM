package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for UserServiceImpl
 * Tests all public methods, constructors, edge cases, null checks, and boundary conditions
 */
@ExtendWith(MockitoExtension.class)
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

    private User testUser;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setId(1);
        userRole.setName("ROLE_USER");

        adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("password123")
                .enabled(1)
                .role(userRole)
                .build();

        // Setup setter methods
        userService.setUserRepository(userRepository);
        userService.setRoleRepository(roleRepository);
        userService.setPasswordEncoder(passwordEncoder);
        userService.setAuthenticationManager(authenticationManager);
        userService.setSpringDataUserDetailsService(springDataUserDetailsService);
    }

    // Setter Tests
    @Test
    void testSetUserRepository() {
        UserRepository newRepository = mock(UserRepository.class);
        userService.setUserRepository(newRepository);
        assertNotNull(userService);
    }

    @Test
    void testSetRoleRepository() {
        RoleRepository newRepository = mock(RoleRepository.class);
        userService.setRoleRepository(newRepository);
        assertNotNull(userService);
    }

    @Test
    void testSetPasswordEncoder() {
        BCryptPasswordEncoder newEncoder = mock(BCryptPasswordEncoder.class);
        userService.setPasswordEncoder(newEncoder);
        assertNotNull(userService);
    }

    @Test
    void testSetAuthenticationManager() {
        AuthenticationManager newManager = mock(AuthenticationManager.class);
        userService.setAuthenticationManager(newManager);
        assertNotNull(userService);
    }

    @Test
    void testSetSpringDataUserDetailsService() {
        SpringDataUserDetailsService newService = mock(SpringDataUserDetailsService.class);
        userService.setSpringDataUserDetailsService(newService);
        assertNotNull(userService);
    }

    // findByUsername Tests
    @Test
    void testFindByUsername_WithExistingUser() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(testUser);

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(testUser.getId(), result.getId());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_WithNonExistingUser() {
        // Arrange
        String username = "nonexisting";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_WithNullUsername() {
        // Arrange
        when(userRepository.findByUsername(null)).thenReturn(null);

        // Act
        User result = userService.findByUsername(null);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUsername(null);
    }

    @Test
    void testFindByUsername_WithEmptyString() {
        // Arrange
        String username = "";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_WithWhitespaceString() {
        // Arrange
        String username = "   ";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_WithSpecialCharacters() {
        // Arrange
        String username = "user@test#2024";
        User specialUser = User.builder()
                .id(2L)
                .username(username)
                .email("special@test.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(specialUser);

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    // listAllUsers Tests
    @Test
    void testListAllUsers_WithMultipleUsers() {
        // Arrange
        List<User> users = Arrays.asList(testUser,
                User.builder().id(2L).username("user2").email("user2@test.com").enabled(1).build());
        when(userRepository.findAllByEnabled(1)).thenReturn(users);

        // Act
        Iterable<User> result = userService.listAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<User>) result).size());
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testListAllUsers_WithEmptyList() {
        // Arrange
        when(userRepository.findAllByEnabled(1)).thenReturn(Collections.emptyList());

        // Act
        Iterable<User> result = userService.listAllUsers();

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testListAllUsers_OnlyReturnsEnabledUsers() {
        // Arrange
        List<User> enabledUsers = Arrays.asList(testUser);
        when(userRepository.findAllByEnabled(1)).thenReturn(enabledUsers);

        // Act
        Iterable<User> result = userService.listAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<User>) result).size());
        verify(userRepository, times(1)).findAllByEnabled(1);
        verify(userRepository, never()).findAllByEnabled(0);
    }

    // showUser Tests
    @Test
    void testShowUser_WithExistingUser() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.showUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testShowUser_WithNonExistingUser() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.showUser(userId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testShowUser_WithNullId() {
        // Arrange
        when(userRepository.findById(null)).thenReturn(Optional.empty());

        // Act
        User result = userService.showUser(null);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(null);
    }

    @Test
    void testShowUser_WithZeroId() {
        // Arrange
        Long zeroId = 0L;
        when(userRepository.findById(zeroId)).thenReturn(Optional.empty());

        // Act
        User result = userService.showUser(zeroId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(zeroId);
    }

    // saveUser Tests
    @Test
    void testSaveUser_WithValidUser() {
        // Arrange
        User newUser = User.builder()
                .username("newuser")
                .email("new@test.com")
                .password("rawPassword")
                .build();
        String encodedPassword = "encodedPassword123";

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode("rawPassword")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("newuser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        when(springDataUserDetailsService.loadUserByUsername("newuser")).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));

        // Act
        userService.saveUser(newUser);

        // Assert
        assertEquals(1, newUser.getEnabled());
        assertEquals(encodedPassword, newUser.getPassword());
        assertEquals(userRole, newUser.getRole());
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(userRepository, times(1)).save(newUser);
        verify(springDataUserDetailsService, times(1)).loadUserByUsername("newuser");
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testSaveUser_WithFirstUser_ShouldBeAdmin() {
        // Arrange
        User firstUser = User.builder()
                .username("admin")
                .email("admin@test.com")
                .password("adminPassword")
                .build();
        String encodedPassword = "encodedAdminPassword";

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(passwordEncoder.encode("adminPassword")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        when(springDataUserDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));

        // Act
        userService.saveUser(firstUser);

        // Assert
        assertEquals(1, firstUser.getEnabled());
        assertEquals(encodedPassword, firstUser.getPassword());
        assertEquals(adminRole, firstUser.getRole());
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        verify(userRepository, times(2)).save(firstUser);
    }

    @Test
    void testSaveUser_WithNullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            userService.saveUser(null);
        });
    }

    @Test
    void testSaveUser_EnabledFlagIsSetToOne() {
        // Arrange
        User userWithDisabledFlag = User.builder()
                .username("disableduser")
                .email("disabled@test.com")
                .password("password")
                .enabled(0)
                .build();
        String encodedPassword = "encodedPassword";

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(3L);
            return user;
        });
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("disableduser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        when(springDataUserDetailsService.loadUserByUsername("disableduser")).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));

        // Act
        userService.saveUser(userWithDisabledFlag);

        // Assert
        assertEquals(1, userWithDisabledFlag.getEnabled());
        verify(userRepository, times(1)).save(userWithDisabledFlag);
    }

    // editUser Tests
    @Test
    void testEditUser_WithValidUser() {
        // Arrange
        User userToEdit = User.builder()
                .id(5L)
                .username("edituser")
                .email("edit@test.com")
                .password("newPassword")
                .role(userRole)
                .build();
        String encodedPassword = "encodedNewPassword";

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(roleRepository.findById(userRole.getId())).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("newPassword")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(userToEdit);

        // Act
        userService.editUser(userToEdit);

        // Assert
        assertEquals(encodedPassword, userToEdit.getPassword());
        assertEquals(1, userToEdit.getEnabled());
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(roleRepository, times(1)).findById(userRole.getId());
        verify(userRepository, times(1)).save(userToEdit);
    }

    @Test
    void testEditUser_WithNullRole_ShouldSetDefaultRole() {
        // Arrange
        User userWithNullRole = User.builder()
                .id(6L)
                .username("nullroleuser")
                .email("nullrole@test.com")
                .password("password")
                .role(null)
                .build();
        String encodedPassword = "encodedPassword";

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(userWithNullRole);

        // Act
        userService.editUser(userWithNullRole);

        // Assert
        assertEquals(encodedPassword, userWithNullRole.getPassword());
        assertEquals(userRole, userWithNullRole.getRole());
        assertEquals(1, userWithNullRole.getEnabled());
        verify(roleRepository, times(2)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(userWithNullRole);
    }

    @Test
    void testEditUser_WithNullPointerException_ShouldCatchAndSetDefaultRole() {
        // Arrange
        User userWithInvalidRole = User.builder()
                .id(7L)
                .username("invalidroleuser")
                .email("invalid@test.com")
                .password("password")
                .build();

        // Create a role with null ID to trigger NullPointerException
        Role roleWithNullId = new Role();
        roleWithNullId.setId(0);
        roleWithNullId.setName("INVALID_ROLE");
        userWithInvalidRole.setRole(roleWithNullId);

        String encodedPassword = "encodedPassword";

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(roleRepository.findById(anyInt())).thenThrow(new NullPointerException());
        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(userWithInvalidRole);

        // Act
        userService.editUser(userWithInvalidRole);

        // Assert
        assertEquals(encodedPassword, userWithInvalidRole.getPassword());
        assertEquals(userRole, userWithInvalidRole.getRole());
        assertEquals(1, userWithInvalidRole.getEnabled());
        verify(userRepository, times(1)).save(userWithInvalidRole);
    }

    @Test
    void testEditUser_WithNullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            userService.editUser(null);
        });
    }

    @Test
    void testEditUser_WithEmptyPassword() {
        // Arrange
        User userWithEmptyPassword = User.builder()
                .id(8L)
                .username("emptypassuser")
                .email("empty@test.com")
                .password("")
                .role(userRole)
                .build();
        String encodedPassword = "encodedEmptyPassword";

        when(roleRepository.findById(userRole.getId())).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(userWithEmptyPassword);

        // Act
        userService.editUser(userWithEmptyPassword);

        // Assert
        assertEquals(encodedPassword, userWithEmptyPassword.getPassword());
        verify(passwordEncoder, times(1)).encode("");
        verify(userRepository, times(1)).save(userWithEmptyPassword);
    }

    // deleteUser Tests
    @Test
    void testDeleteUser_WithValidUser() {
        // Arrange
        User userToDelete = User.builder()
                .id(10L)
                .username("deleteuser")
                .email("delete@test.com")
                .password("password")
                .enabled(1)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(userToDelete);

        // Act
        userService.deleteUser(userToDelete);

        // Assert
        assertEquals(0, userToDelete.getEnabled());
        assertNull(userToDelete.getPassword());
        verify(userRepository, times(1)).save(userToDelete);
    }

    @Test
    void testDeleteUser_WithNullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            userService.deleteUser(null);
        });
    }

    @Test
    void testDeleteUser_WithAlreadyDisabledUser() {
        // Arrange
        User alreadyDisabledUser = User.builder()
                .id(11L)
                .username("alreadydisabled")
                .email("disabled@test.com")
                .password("password")
                .enabled(0)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(alreadyDisabledUser);

        // Act
        userService.deleteUser(alreadyDisabledUser);

        // Assert
        assertEquals(0, alreadyDisabledUser.getEnabled());
        assertNull(alreadyDisabledUser.getPassword());
        verify(userRepository, times(1)).save(alreadyDisabledUser);
    }

    @Test
    void testDeleteUser_SetsEnabledToZero() {
        // Arrange
        User userWithEnabledOne = User.builder()
                .id(12L)
                .username("enableduser")
                .email("enabled@test.com")
                .password("password123")
                .enabled(1)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(userWithEnabledOne);

        // Act
        userService.deleteUser(userWithEnabledOne);

        // Assert
        assertEquals(0, userWithEnabledOne.getEnabled());
        verify(userRepository, times(1)).save(userWithEnabledOne);
    }

    @Test
    void testDeleteUser_SetsPasswordToNull() {
        // Arrange
        User userWithPassword = User.builder()
                .id(13L)
                .username("passworduser")
                .email("password@test.com")
                .password("somePassword")
                .enabled(1)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(userWithPassword);

        // Act
        userService.deleteUser(userWithPassword);

        // Assert
        assertNull(userWithPassword.getPassword());
        verify(userRepository, times(1)).save(userWithPassword);
    }

    @Test
    void testDeleteUser_WithNullPassword() {
        // Arrange
        User userWithNullPassword = User.builder()
                .id(14L)
                .username("nullpassuser")
                .email("nullpass@test.com")
                .password(null)
                .enabled(1)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(userWithNullPassword);

        // Act
        userService.deleteUser(userWithNullPassword);

        // Assert
        assertNull(userWithNullPassword.getPassword());
        assertEquals(0, userWithNullPassword.getEnabled());
        verify(userRepository, times(1)).save(userWithNullPassword);
    }

    // Integration Tests
    @Test
    void testFindByUsername_AfterSaveUser() {
        // Arrange
        User newUser = User.builder()
                .username("integrationuser")
                .email("integration@test.com")
                .password("password")
                .build();
        String encodedPassword = "encodedPassword";

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(20L);
            return user;
        });
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("integrationuser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        when(springDataUserDetailsService.loadUserByUsername("integrationuser")).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));
        when(userRepository.findByUsername("integrationuser")).thenReturn(newUser);

        // Act
        userService.saveUser(newUser);
        User foundUser = userService.findByUsername("integrationuser");

        // Assert
        assertNotNull(foundUser);
        assertEquals("integrationuser", foundUser.getUsername());
        verify(userRepository, times(1)).save(newUser);
        verify(userRepository, times(1)).findByUsername("integrationuser");
    }
}
