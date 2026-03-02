package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserServiceImpl service;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UserServiceImpl();
        service.setUserRepository(userRepository);
        service.setRoleRepository(roleRepository);
        service.setPasswordEncoder(passwordEncoder);
        service.setAuthenticationManager(authenticationManager);
        service.setSpringDataUserDetailsService(springDataUserDetailsService);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        when(userRepository.findByUsername("test")).thenReturn(user);

        User result = service.findByUsername("test");
        assertNotNull(result);
        verify(userRepository).findByUsername("test");
    }

    @Test
    void testListAllUsers() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAllByEnabled(1)).thenReturn(users);

        Iterable<User> result = service.listAllUsers();
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void testShowUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = service.showUser(1L);
        assertNotNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setPassword("test");
        when(userRepository.save(user)).thenReturn(user);

        service.deleteUser(user);

        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testShowUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User result = service.showUser(999L);
        assertNull(result);
    }
}
