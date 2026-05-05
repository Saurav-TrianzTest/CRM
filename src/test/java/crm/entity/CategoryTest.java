package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void constructor_shouldCreateCategory() {
        // Assert
        assertNotNull(category);
    }

    @Test
    void setId_shouldSetId() {
        // Act
        category.setId(1L);
        
        // Assert
        assertEquals(1L, category.getId());
    }

    @Test
    void setName_shouldSetName() {
        // Act
        category.setName("Test Category");
        
        // Assert
        assertEquals("Test Category", category.getName());
    }

    @Test
    void getId_shouldReturnId() {
        // Arrange
        category.setId(5L);
        
        // Act
        Long id = category.getId();
        
        // Assert
        assertEquals(5L, id);
    }

    @Test
    void getName_shouldReturnName() {
        // Arrange
        category.setName("Premium");
        
        // Act
        String name = category.getName();
        
        // Assert
        assertEquals("Premium", name);
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Test");
        
        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Test");
        
        // Assert
        assertEquals(category1, category2);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Test");
        
        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Test");
        
        // Assert
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    void toString_shouldReturnStringRepresentation() {
        // Arrange
        category.setId(1L);
        category.setName("Test Category");
        
        // Act
        String result = category.toString();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Test Category"));
    }
}
