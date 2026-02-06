package crm.repository;

import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        User user = User.builder().id(1L).username("testuser").build();

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userRepository.findByUsername("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindAllByEnabled() {
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().id(1L).username("user1").enabled(1).build());

        when(userRepository.findAllByEnabled(1)).thenReturn(users);

        Iterable<User> result = userRepository.findAllByEnabled(1);
        assertNotNull(result);
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testFindByUsernameReturnsNull() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);
        User result = userRepository.findByUsername("nonexistent");
        assertNull(result);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(userRepository);
    }
}
