package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Category Entity Tests")
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("VIP");
    }

    @Test
    @DisplayName("Test Category constructor")
    void testCategoryConstructor() {
        Category newCategory = new Category();
        assertNotNull(newCategory);
        assertNull(newCategory.getId());
        assertNull(newCategory.getName());
    }

    @Test
    @DisplayName("Test Category getters and setters")
    void testGettersAndSetters() {
        Category testCategory = new Category();
        testCategory.setId(2L);
        testCategory.setName("Premium");

        assertEquals(2L, testCategory.getId());
        assertEquals("Premium", testCategory.getName());
    }

    @Test
    @DisplayName("Test Category with valid data")
    void testCategoryWithValidData() {
        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("VIP", category.getName());
    }

    @Test
    @DisplayName("Test Category with null values")
    void testCategoryWithNullValues() {
        Category nullCategory = new Category();
        nullCategory.setId(null);
        nullCategory.setName(null);

        assertNull(nullCategory.getId());
        assertNull(nullCategory.getName());
    }

    @Test
    @DisplayName("Test Category with empty name")
    void testCategoryWithEmptyName() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    @DisplayName("Test Category with long name")
    void testCategoryWithLongName() {
        String longName = "A".repeat(255);
        category.setName(longName);
        assertEquals(longName, category.getName());
        assertEquals(255, category.getName().length());
    }

    @Test
    @DisplayName("Test Category name with special characters")
    void testCategoryNameWithSpecialCharacters() {
        category.setName("VIP-Premium-Gold");
        assertEquals("VIP-Premium-Gold", category.getName());
    }

    @Test
    @DisplayName("Test Category name with numbers")
    void testCategoryNameWithNumbers() {
        category.setName("Level1");
        assertEquals("Level1", category.getName());
    }

    @Test
    @DisplayName("Test Category name with spaces")
    void testCategoryNameWithSpaces() {
        category.setName("VIP Customer");
        assertEquals("VIP Customer", category.getName());
    }

    @Test
    @DisplayName("Test Category equals and hashCode")
    void testEqualsAndHashCode() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("VIP");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("VIP");

        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    @DisplayName("Test Category not equals with different id")
    void testNotEqualsWithDifferentId() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("VIP");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("VIP");

        assertNotEquals(category1, category2);
    }

    @Test
    @DisplayName("Test Category not equals with different name")
    void testNotEqualsWithDifferentName() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("VIP");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Premium");

        assertNotEquals(category1, category2);
    }

    @Test
    @DisplayName("Test Category toString")
    void testToString() {
        String categoryString = category.toString();
        assertNotNull(categoryString);
        assertTrue(categoryString.contains("VIP") || categoryString.contains("1"));
    }

    @Test
    @DisplayName("Test Category id with zero")
    void testCategoryIdWithZero() {
        category.setId(0L);
        assertEquals(0L, category.getId());
    }

    @Test
    @DisplayName("Test Category id with large value")
    void testCategoryIdWithLargeValue() {
        Long largeId = Long.MAX_VALUE;
        category.setId(largeId);
        assertEquals(largeId, category.getId());
    }

    @Test
    @DisplayName("Test Category name update")
    void testCategoryNameUpdate() {
        assertEquals("VIP", category.getName());
        category.setName("Gold");
        assertEquals("Gold", category.getName());
    }

    @Test
    @DisplayName("Test Category id update")
    void testCategoryIdUpdate() {
        assertEquals(1L, category.getId());
        category.setId(100L);
        assertEquals(100L, category.getId());
    }

    @Test
    @DisplayName("Test Category with uppercase name")
    void testCategoryWithUppercaseName() {
        category.setName("PREMIUM");
        assertEquals("PREMIUM", category.getName());
    }

    @Test
    @DisplayName("Test Category with lowercase name")
    void testCategoryWithLowercaseName() {
        category.setName("premium");
        assertEquals("premium", category.getName());
    }

    @Test
    @DisplayName("Test Category with mixed case name")
    void testCategoryWithMixedCaseName() {
        category.setName("PrEmIuM");
        assertEquals("PrEmIuM", category.getName());
    }
}
