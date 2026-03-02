package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void testUserRepositoryInterface() {
        assertNotNull(UserRepository.class);
    }

    @Test
    void testFindByUsernameMethodExists() throws NoSuchMethodException {
        UserRepository.class.getMethod("findByUsername", String.class);
    }

    @Test
    void testFindAllByEnabledMethodExists() throws NoSuchMethodException {
        UserRepository.class.getMethod("findAllByEnabled", int.class);
    }
}
