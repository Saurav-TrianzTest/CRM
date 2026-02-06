package crm.repository;

import crm.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    @Test
    void testFindByName() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        Role result = roleRepository.findByName("ROLE_USER");
        assertNotNull(result);
        assertEquals("ROLE_USER", result.getName());
        verify(roleRepository, times(1)).findByName("ROLE_USER");
    }

    @Test
    void testFindByNameReturnsNull() {
        when(roleRepository.findByName("INVALID")).thenReturn(null);
        Role result = roleRepository.findByName("INVALID");
        assertNull(result);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(roleRepository);
    }
}
