package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Category Entity Tests")
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    @DisplayName("Test default constructor creates non-null instance")
    void testDefaultConstructor() {
        Category newCategory = new Category();
        assertNotNull(newCategory);
    }

    @Test
    @DisplayName("Test setId and getId")
    void testSetAndGetId() {
        Long id = 1L;
        category.setId(id);
        assertEquals(id, category.getId());
    }

    @Test
    @DisplayName("Test setId with null value")
    void testSetIdNull() {
        category.setId(null);
        assertNull(category.getId());
    }

    @Test
    @DisplayName("Test setId with zero")
    void testSetIdZero() {
        Long id = 0L;
        category.setId(id);
        assertEquals(id, category.getId());
    }

    @Test
    @DisplayName("Test setId with negative value")
    void testSetIdNegative() {
        Long id = -1L;
        category.setId(id);
        assertEquals(id, category.getId());
    }

    @Test
    @DisplayName("Test setId with maximum Long value")
    void testSetIdMaximum() {
        Long id = Long.MAX_VALUE;
        category.setId(id);
        assertEquals(id, category.getId());
    }

    @Test
    @DisplayName("Test setId with minimum Long value")
    void testSetIdMinimum() {
        Long id = Long.MIN_VALUE;
        category.setId(id);
        assertEquals(id, category.getId());
    }

    @Test
    @DisplayName("Test setName and getName")
    void testSetAndGetName() {
        String name = "Technology";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test setName with null value")
    void testSetNameNull() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    @DisplayName("Test setName with empty string")
    void testSetNameEmpty() {
        String name = "";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test setName with single character")
    void testSetNameSingleCharacter() {
        String name = "A";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test setName with long string")
    void testSetNameLongString() {
        String name = "A".repeat(255);
        category.setName(name);
        assertEquals(name, category.getName());
        assertEquals(255, category.getName().length());
    }

    @Test
    @DisplayName("Test setName with special characters")
    void testSetNameSpecialCharacters() {
        String name = "Tech & IT Solutions";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test setName with numbers")
    void testSetNameWithNumbers() {
        String name = "Category123";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test setName with spaces")
    void testSetNameWithSpaces() {
        String name = "   Category with spaces   ";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test setName with unicode characters")
    void testSetNameWithUnicode() {
        String name = "Catégorie";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test setName with emoji")
    void testSetNameWithEmoji() {
        String name = "Tech Category";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Test equals with same object")
    void testEqualsWithSameObject() {
        category.setId(1L);
        category.setName("Technology");
        assertEquals(category, category);
    }

    @Test
    @DisplayName("Test equals with equal objects")
    void testEqualsWithEqualObjects() {
        category.setId(1L);
        category.setName("Technology");

        Category other = new Category();
        other.setId(1L);
        other.setName("Technology");

        assertEquals(category, other);
    }

    @Test
    @DisplayName("Test equals with different ids")
    void testEqualsWithDifferentIds() {
        category.setId(1L);
        category.setName("Technology");

        Category other = new Category();
        other.setId(2L);
        other.setName("Technology");

        assertNotEquals(category, other);
    }

    @Test
    @DisplayName("Test equals with different names")
    void testEqualsWithDifferentNames() {
        category.setId(1L);
        category.setName("Technology");

        Category other = new Category();
        other.setId(1L);
        other.setName("Business");

        assertNotEquals(category, other);
    }

    @Test
    @DisplayName("Test equals with null")
    void testEqualsWithNull() {
        category.setId(1L);
        assertNotEquals(null, category);
    }

    @Test
    @DisplayName("Test equals with different class")
    void testEqualsWithDifferentClass() {
        category.setId(1L);
        assertNotEquals("String object", category);
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        category.setId(1L);
        category.setName("Technology");

        int hashCode1 = category.hashCode();
        int hashCode2 = category.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test hashCode with equal objects")
    void testHashCodeWithEqualObjects() {
        category.setId(1L);
        category.setName("Technology");

        Category other = new Category();
        other.setId(1L);
        other.setName("Technology");

        assertEquals(category.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test hashCode with different objects")
    void testHashCodeWithDifferentObjects() {
        category.setId(1L);
        category.setName("Technology");

        Category other = new Category();
        other.setId(2L);
        other.setName("Business");

        assertNotEquals(category.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test toString contains field values")
    void testToString() {
        category.setId(1L);
        category.setName("Technology");

        String toString = category.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Technology"));
    }

    @Test
    @DisplayName("Test toString with null name")
    void testToStringWithNullName() {
        category.setId(1L);
        category.setName(null);

        String toString = category.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("null"));
    }

    @Test
    @DisplayName("Test toString with null id")
    void testToStringWithNullId() {
        category.setId(null);
        category.setName("Technology");

        String toString = category.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Technology"));
    }

    @Test
    @DisplayName("Test toString with all null fields")
    void testToStringWithAllNullFields() {
        String toString = category.toString();

        assertNotNull(toString);
    }

    @Test
    @DisplayName("Test multiple updates to same category")
    void testMultipleUpdates() {
        category.setId(1L);
        category.setName("Initial Name");

        assertEquals(1L, category.getId());
        assertEquals("Initial Name", category.getName());

        category.setName("Updated Name");
        assertEquals("Updated Name", category.getName());

        category.setId(2L);
        assertEquals(2L, category.getId());
    }

    @Test
    @DisplayName("Test category with business domain name")
    void testCategoryBusinessDomain() {
        category.setName("Enterprise Solutions");
        assertEquals("Enterprise Solutions", category.getName());
    }

    @Test
    @DisplayName("Test category with technology domain name")
    void testCategoryTechnologyDomain() {
        category.setName("Cloud Computing");
        assertEquals("Cloud Computing", category.getName());
    }

    @Test
    @DisplayName("Test category with finance domain name")
    void testCategoryFinanceDomain() {
        category.setName("Financial Services");
        assertEquals("Financial Services", category.getName());
    }

    @Test
    @DisplayName("Test category with retail domain name")
    void testCategoryRetailDomain() {
        category.setName("E-commerce");
        assertEquals("E-commerce", category.getName());
    }

    @Test
    @DisplayName("Test category state after multiple name changes")
    void testMultipleNameChanges() {
        category.setId(1L);
        category.setName("Name1");
        category.setName("Name2");
        category.setName("Name3");

        assertEquals(1L, category.getId());
        assertEquals("Name3", category.getName());
    }

    @Test
    @DisplayName("Test category state after id reset")
    void testIdReset() {
        category.setId(1L);
        category.setName("Technology");

        assertEquals(1L, category.getId());

        category.setId(null);
        assertNull(category.getId());
        assertEquals("Technology", category.getName());
    }

    @Test
    @DisplayName("Test category with name containing newlines")
    void testNameWithNewlines() {
        String name = "Multi\nLine\nCategory";
        category.setName(name);
        assertEquals(name, category.getName());
        assertTrue(category.getName().contains("\n"));
    }

    @Test
    @DisplayName("Test category with name containing tabs")
    void testNameWithTabs() {
        String name = "Tab\tSeparated\tCategory";
        category.setName(name);
        assertEquals(name, category.getName());
        assertTrue(category.getName().contains("\t"));
    }

    @Test
    @DisplayName("Test category immutability of id after setting")
    void testIdImmutability() {
        Long originalId = 5L;
        category.setId(originalId);

        Long retrievedId = category.getId();
        assertEquals(originalId, retrievedId);

        category.setName("Some Category");
        assertEquals(originalId, category.getId());
    }

    @Test
    @DisplayName("Test category complete lifecycle")
    void testCategoryLifecycle() {
        assertNull(category.getId());
        assertNull(category.getName());

        category.setId(1L);
        category.setName("New Category");

        assertEquals(1L, category.getId());
        assertEquals("New Category", category.getName());

        category.setName("Updated Category");
        assertEquals("Updated Category", category.getName());
    }
}
