package crm.repository;

import crm.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest {

    @Test
    void roleRepository_shouldExtendJpaRepository() {
        // Assert
        assertTrue(JpaRepository.class.isAssignableFrom(RoleRepository.class));
    }

    @Test
    void findByName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            RoleRepository.class.getMethod("findByName", String.class);
        });
    }

    @Test
    void findByName_shouldReturnRole() throws NoSuchMethodException {
        // Arrange
        var method = RoleRepository.class.getMethod("findByName", String.class);
        
        // Assert
        assertEquals(Role.class, method.getReturnType());
    }

    @Test
    void repository_shouldHaveRepositoryAnnotation() {
        // Assert
        assertTrue(RoleRepository.class.isAnnotationPresent(
            org.springframework.stereotype.Repository.class));
    }
}
