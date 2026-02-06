package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(category);
    }

    @Test
    void testGettersAndSetters() {
        Long id = 1L;
        String name = "Premium";

        category.setId(id);
        category.setName(name);

        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
    }

    @Test
    void testSetId() {
        Long id = 100L;
        category.setId(id);

        assertEquals(id, category.getId());
    }

    @Test
    void testSetName() {
        String name = "VIP";
        category.setName(name);

        assertEquals(name, category.getName());
    }

    @Test
    void testNullValues() {
        category.setId(null);
        category.setName(null);

        assertNull(category.getId());
        assertNull(category.getName());
    }

    @Test
    void testEmptyName() {
        category.setName("");

        assertEquals("", category.getName());
    }

    @Test
    void testLongName() {
        String longName = "A".repeat(255);
        category.setName(longName);

        assertEquals(longName, category.getName());
    }

    @Test
    void testEquality() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Test");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Test");

        assertEquals(category1, category2);
    }

    @Test
    void testHashCode() {
        category.setId(1L);
        category.setName("Test");

        int hashCode1 = category.hashCode();
        int hashCode2 = category.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testToString() {
        category.setId(1L);
        category.setName("Premium");

        String toString = category.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Premium"));
    }
}
