package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest {

    @Test
    void testRoleRepositoryInterface() {
        assertNotNull(RoleRepository.class);
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        RoleRepository.class.getMethod("findByName", String.class);
    }
}
