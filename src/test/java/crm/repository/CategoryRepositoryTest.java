package crm.repository;

import crm.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {

    @Test
    void categoryRepository_shouldExtendJpaRepository() {
        // Assert
        assertTrue(JpaRepository.class.isAssignableFrom(CategoryRepository.class));
    }

    @Test
    void findByName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CategoryRepository.class.getMethod("findByName", String.class);
        });
    }

    @Test
    void findByName_shouldReturnCategory() throws NoSuchMethodException {
        // Arrange
        var method = CategoryRepository.class.getMethod("findByName", String.class);
        
        // Assert
        assertEquals(Category.class, method.getReturnType());
    }

    @Test
    void repository_shouldHaveRepositoryAnnotation() {
        // Assert
        assertTrue(CategoryRepository.class.isAnnotationPresent(
            org.springframework.stereotype.Repository.class));
    }
}
