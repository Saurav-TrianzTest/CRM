package crm.repository;

import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .enabled(1)
                .build();
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userRepository.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        User result = userRepository.findByUsername("nonexistent");

        assertNull(result);
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void testFindAllByEnabled() {
        List<User> users = Arrays.asList(user);

        when(userRepository.findAllByEnabled(1)).thenReturn(users);

        Iterable<User> result = userRepository.findAllByEnabled(1);

        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void testFindAllByDisabled() {
        User disabledUser = User.builder()
                .id(2L)
                .username("disabled")
                .enabled(0)
                .build();

        List<User> users = Arrays.asList(disabledUser);

        when(userRepository.findAllByEnabled(0)).thenReturn(users);

        Iterable<User> result = userRepository.findAllByEnabled(0);

        assertNotNull(result);
        verify(userRepository).findAllByEnabled(0);
    }

    @Test
    void testFindAll() {
        List<User> users = Arrays.asList(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testSave() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userRepository.save(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(999L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(999L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(userRepository).deleteById(1L);

        userRepository.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testCount() {
        when(userRepository.count()).thenReturn(50L);

        long count = userRepository.count();

        assertEquals(50L, count);
        verify(userRepository).count();
    }

    @Test
    void testExistsById() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean exists = userRepository.existsById(1L);

        assertTrue(exists);
        verify(userRepository).existsById(1L);
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).delete(user);

        userRepository.delete(user);

        verify(userRepository).delete(user);
    }
}
