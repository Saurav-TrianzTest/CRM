package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryRepositoryTest {

    @Test
    public void testCategoryRepositoryInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.repository.CategoryRepository"));
    }

    @Test
    public void testCategoryRepositoryHasFindByNameMethod() throws NoSuchMethodException {
        assertNotNull(CategoryRepository.class.getMethod("findByName", String.class));
    }
}
