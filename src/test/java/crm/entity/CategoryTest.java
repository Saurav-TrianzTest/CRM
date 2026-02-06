package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
    }

    @Test
    public void testCategoryConstructor() {
        assertNotNull(category);
    }

    @Test
    public void testSetAndGetId() {
        category.setId(1L);
        assertEquals(1L, category.getId());
    }

    @Test
    public void testSetAndGetName() {
        category.setName("VIP");
        assertEquals("VIP", category.getName());
    }

    @Test
    public void testCategoryWithNullName() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    public void testCategoryWithEmptyName() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    public void testCategoryIdBoundary() {
        category.setId(0L);
        assertEquals(0L, category.getId());
        category.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, category.getId());
    }

    @Test
    public void testCategoryEquality() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Premium");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Premium");

        assertEquals(category1.getId(), category2.getId());
        assertEquals(category1.getName(), category2.getName());
    }

    @Test
    public void testCategoryToString() {
        category.setId(1L);
        category.setName("Standard");
        String result = category.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Standard"));
    }
}
