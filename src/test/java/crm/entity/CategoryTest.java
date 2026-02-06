package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void testCategoryCreation() {
        assertNotNull(category);
    }

    @Test
    void testSetAndGetId() {
        category.setId(1L);
        assertEquals(1L, category.getId());
    }

    @Test
    void testSetAndGetName() {
        category.setName("Premium");
        assertEquals("Premium", category.getName());
    }

    @Test
    void testCategoryWithNullName() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    void testCategoryWithEmptyName() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    void testCategoryWithDifferentIds() {
        category.setId(10L);
        assertEquals(10L, category.getId());

        category.setId(100L);
        assertEquals(100L, category.getId());
    }

    @Test
    void testCategoryWithMultipleNames() {
        category.setName("Basic");
        assertEquals("Basic", category.getName());

        category.setName("Premium");
        assertEquals("Premium", category.getName());
    }

    @Test
    void testCategoryEquality() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Premium");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Premium");

        assertEquals(category1, category2);
    }

    @Test
    void testCategoryHashCode() {
        category.setId(1L);
        category.setName("Premium");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Premium");

        assertEquals(category.hashCode(), category2.hashCode());
    }

    @Test
    void testCategoryToString() {
        category.setId(1L);
        category.setName("Premium");

        String result = category.toString();
        assertNotNull(result);
        assertTrue(result.contains("Premium"));
    }

    @Test
    void testCategoryWithLongName() {
        String longName = "ThisIsAVeryLongCategoryNameForTestingPurposes";
        category.setName(longName);
        assertEquals(longName, category.getName());
    }
}
