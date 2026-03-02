package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {

    @Test
    void testCategoryRepositoryInterface() {
        assertNotNull(CategoryRepository.class);
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        CategoryRepository.class.getMethod("findByName", String.class);
    }
}
