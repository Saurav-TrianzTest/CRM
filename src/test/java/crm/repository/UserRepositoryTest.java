package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    @Test
    public void testUserRepositoryInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.repository.UserRepository"));
    }

    @Test
    public void testUserRepositoryHasFindByUsernameMethod() throws NoSuchMethodException {
        assertNotNull(UserRepository.class.getMethod("findByUsername", String.class));
    }

    @Test
    public void testUserRepositoryHasFindAllByEnabledMethod() throws NoSuchMethodException {
        assertNotNull(UserRepository.class.getMethod("findAllByEnabled", int.class));
    }
}
