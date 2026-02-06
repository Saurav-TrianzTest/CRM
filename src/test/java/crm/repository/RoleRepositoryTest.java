package crm.repository;

import crm.entity.Role;
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
class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testFindByName() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(role);

        Role result = roleRepository.findByName("ROLE_ADMIN");

        assertNotNull(result);
        assertEquals("ROLE_ADMIN", result.getName());
        verify(roleRepository).findByName("ROLE_ADMIN");
    }

    @Test
    void testFindByNameNotFound() {
        when(roleRepository.findByName("NONEXISTENT")).thenReturn(null);

        Role result = roleRepository.findByName("NONEXISTENT");

        assertNull(result);
        verify(roleRepository).findByName("NONEXISTENT");
    }

    @Test
    void testFindAll() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_USER");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleRepository).findAll();
    }

    @Test
    void testSave() {
        Role role = new Role();
        role.setName("ROLE_MANAGER");

        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleRepository.save(role);

        assertNotNull(result);
        assertEquals("ROLE_MANAGER", result.getName());
        verify(roleRepository).save(role);
    }

    @Test
    void testFindById() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        Optional<Role> result = roleRepository.findById(1);

        assertTrue(result.isPresent());
        assertEquals("ROLE_ADMIN", result.get().getName());
        verify(roleRepository).findById(1);
    }

    @Test
    void testDeleteById() {
        doNothing().when(roleRepository).deleteById(1);

        roleRepository.deleteById(1);

        verify(roleRepository).deleteById(1);
    }

    @Test
    void testCount() {
        when(roleRepository.count()).thenReturn(4L);

        long count = roleRepository.count();

        assertEquals(4L, count);
        verify(roleRepository).count();
    }

    @Test
    void testExistsById() {
        when(roleRepository.existsById(1)).thenReturn(true);

        boolean exists = roleRepository.existsById(1);

        assertTrue(exists);
        verify(roleRepository).existsById(1);
    }
}
