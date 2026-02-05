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
    public void testCategoryCreation() {
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
    public void testCategoryEquality() {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("Premium");

        Category c2 = new Category();
        c2.setId(1L);
        c2.setName("Premium");

        assertEquals(c1, c2);
    }

    @Test
    public void testCategoryHashCode() {
        category.setId(1L);
        category.setName("Standard");
        int hashCode = category.hashCode();
        assertTrue(hashCode != 0);
    }

    @Test
    public void testCategoryToString() {
        category.setId(5L);
        category.setName("Gold");
        String str = category.toString();
        assertNotNull(str);
        assertTrue(str.contains("Gold"));
    }
}
