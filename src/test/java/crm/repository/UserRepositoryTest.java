package crm.repository;

import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void userRepository_shouldExtendJpaRepository() {
        // Assert
        assertTrue(JpaRepository.class.isAssignableFrom(UserRepository.class));
    }

    @Test
    void findByUsername_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserRepository.class.getMethod("findByUsername", String.class);
        });
    }

    @Test
    void findByUsername_shouldReturnUser() throws NoSuchMethodException {
        // Arrange
        var method = UserRepository.class.getMethod("findByUsername", String.class);
        
        // Assert
        assertEquals(User.class, method.getReturnType());
    }

    @Test
    void findAllByEnabled_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            UserRepository.class.getMethod("findAllByEnabled", int.class);
        });
    }

    @Test
    void findAllByEnabled_shouldReturnIterable() throws NoSuchMethodException {
        // Arrange
        var method = UserRepository.class.getMethod("findAllByEnabled", int.class);
        
        // Assert
        assertEquals(Iterable.class, method.getReturnType());
    }

    @Test
    void repository_shouldHaveRepositoryAnnotation() {
        // Assert
        assertTrue(UserRepository.class.isAnnotationPresent(
            org.springframework.stereotype.Repository.class));
    }
}
