package crm.repository;

import crm.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("CategoryRepository Tests")
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Test CategoryRepository extends JpaRepository")
    void testCategoryRepositoryExtendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(CategoryRepository.class));
    }

    @Test
    @DisplayName("Test CategoryRepository is not null")
    void testCategoryRepositoryNotNull() {
        assertNotNull(categoryRepository);
    }

    @Test
    @DisplayName("Test findByName returns correct category")
    void testFindByName() {
        Category category = new Category();
        category.setName("VIP");
        entityManager.persist(category);
        entityManager.flush();

        Category found = categoryRepository.findByName("VIP");

        assertNotNull(found);
        assertEquals("VIP", found.getName());
    }

    @Test
    @DisplayName("Test findByName returns null for non-existent category")
    void testFindByNameNotFound() {
        Category found = categoryRepository.findByName("Non-existent");
        assertNull(found);
    }

    @Test
    @DisplayName("Test findByName with empty string")
    void testFindByNameEmptyString() {
        Category category = new Category();
        category.setName("");
        entityManager.persist(category);
        entityManager.flush();

        Category found = categoryRepository.findByName("");
        assertNotNull(found);
        assertEquals("", found.getName());
    }

    @Test
    @DisplayName("Test save category")
    void testSaveCategory() {
        Category category = new Category();
        category.setName("Premium");

        Category saved = categoryRepository.save(category);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Premium", saved.getName());
    }

    @Test
    @DisplayName("Test save category with null name")
    void testSaveCategoryWithNullName() {
        Category category = new Category();
        category.setName(null);

        Category saved = categoryRepository.save(category);

        assertNotNull(saved);
        assertNull(saved.getName());
    }

    @Test
    @DisplayName("Test findById returns correct category")
    void testFindById() {
        Category category = new Category();
        category.setName("Gold");
        Category saved = entityManager.persist(category);
        entityManager.flush();

        Category found = categoryRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Gold", found.getName());
    }

    @Test
    @DisplayName("Test findById returns empty for non-existent ID")
    void testFindByIdNotFound() {
        Category found = categoryRepository.findById(999999L).orElse(null);
        assertNull(found);
    }

    @Test
    @DisplayName("Test findAll returns all categories")
    void testFindAll() {
        Category category1 = new Category();
        category1.setName("Category1");
        entityManager.persist(category1);

        Category category2 = new Category();
        category2.setName("Category2");
        entityManager.persist(category2);
        entityManager.flush();

        List<Category> categories = categoryRepository.findAll();

        assertNotNull(categories);
        assertTrue(categories.size() >= 2);
    }

    @Test
    @DisplayName("Test delete category")
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("Delete Category");
        Category saved = entityManager.persist(category);
        entityManager.flush();

        Long categoryId = saved.getId();
        categoryRepository.deleteById(categoryId);

        Category deleted = categoryRepository.findById(categoryId).orElse(null);
        assertNull(deleted);
    }

    @Test
    @DisplayName("Test update category name")
    void testUpdateCategoryName() {
        Category category = new Category();
        category.setName("Original Name");
        Category saved = entityManager.persist(category);
        entityManager.flush();

        saved.setName("Updated Name");
        Category updated = categoryRepository.save(saved);

        assertEquals("Updated Name", updated.getName());
    }

    @Test
    @DisplayName("Test count categories")
    void testCountCategories() {
        long initialCount = categoryRepository.count();

        Category category = new Category();
        category.setName("Count Category");
        entityManager.persist(category);
        entityManager.flush();

        long newCount = categoryRepository.count();

        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @DisplayName("Test existsById returns true for existing category")
    void testExistsByIdTrue() {
        Category category = new Category();
        category.setName("Exists Category");
        Category saved = entityManager.persist(category);
        entityManager.flush();

        assertTrue(categoryRepository.existsById(saved.getId()));
    }

    @Test
    @DisplayName("Test existsById returns false for non-existent category")
    void testExistsByIdFalse() {
        assertFalse(categoryRepository.existsById(999999L));
    }

    @Test
    @DisplayName("Test save category with long name")
    void testSaveCategoryWithLongName() {
        String longName = "A".repeat(255);
        Category category = new Category();
        category.setName(longName);

        Category saved = categoryRepository.save(category);

        assertNotNull(saved);
        assertEquals(longName, saved.getName());
    }

    @Test
    @DisplayName("Test findByName with special characters")
    void testFindByNameWithSpecialCharacters() {
        Category category = new Category();
        category.setName("VIP-Premium_Gold");
        entityManager.persist(category);
        entityManager.flush();

        Category found = categoryRepository.findByName("VIP-Premium_Gold");

        assertNotNull(found);
        assertEquals("VIP-Premium_Gold", found.getName());
    }

    @Test
    @DisplayName("Test save multiple categories and find by name")
    void testSaveMultipleCategoriesAndFindByName() {
        Category category1 = new Category();
        category1.setName("Bronze");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Silver");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("Gold");
        categoryRepository.save(category3);

        Category foundBronze = categoryRepository.findByName("Bronze");
        Category foundSilver = categoryRepository.findByName("Silver");
        Category foundGold = categoryRepository.findByName("Gold");

        assertNotNull(foundBronze);
        assertNotNull(foundSilver);
        assertNotNull(foundGold);
        assertEquals("Bronze", foundBronze.getName());
        assertEquals("Silver", foundSilver.getName());
        assertEquals("Gold", foundGold.getName());
    }

    @Test
    @DisplayName("Test findByName is case sensitive")
    void testFindByNameCaseSensitive() {
        Category category = new Category();
        category.setName("VIP");
        entityManager.persist(category);
        entityManager.flush();

        Category foundExact = categoryRepository.findByName("VIP");
        Category foundLower = categoryRepository.findByName("vip");

        assertNotNull(foundExact);
        assertNull(foundLower);
    }
}
