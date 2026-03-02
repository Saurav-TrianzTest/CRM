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
    void testSetAndGetId() {
        category.setId(1L);
        assertEquals(1L, category.getId());
    }

    @Test
    void testSetAndGetName() {
        category.setName("Technology");
        assertEquals("Technology", category.getName());
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(category);
        assertNull(category.getId());
        assertNull(category.getName());
    }

    @Test
    void testSetNameWithNull() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    void testSetNameWithEmptyString() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    void testSetIdWithNull() {
        category.setId(null);
        assertNull(category.getId());
    }

    @Test
    void testSetIdWithZero() {
        category.setId(0L);
        assertEquals(0L, category.getId());
    }

    @Test
    void testMultipleCategoryObjects() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Tech");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Finance");

        assertNotEquals(category1.getId(), category2.getId());
        assertNotEquals(category1.getName(), category2.getName());
    }
}
