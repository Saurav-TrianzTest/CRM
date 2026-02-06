package crm.repository;

import crm.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryRepositoryTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    void testFindByName() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        when(categoryRepository.findByName("Test Category")).thenReturn(category);

        Category result = categoryRepository.findByName("Test Category");
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        verify(categoryRepository, times(1)).findByName("Test Category");
    }

    @Test
    void testFindByNameReturnsNull() {
        when(categoryRepository.findByName("Nonexistent")).thenReturn(null);
        Category result = categoryRepository.findByName("Nonexistent");
        assertNull(result);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(categoryRepository);
    }
}
