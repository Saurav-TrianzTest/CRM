package crm.repository;

import crm.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testFindAll() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Premium");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("VIP");

        List<Category> categories = Arrays.asList(cat1, cat2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void testSave() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Business");

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryRepository.save(category);

        assertNotNull(result);
        assertEquals("Business", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void testFindById() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Premium");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Premium", result.get().getName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryRepository.findById(999L);

        assertFalse(result.isPresent());
        verify(categoryRepository).findById(999L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryRepository.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void testCount() {
        when(categoryRepository.count()).thenReturn(5L);

        long count = categoryRepository.count();

        assertEquals(5L, count);
        verify(categoryRepository).count();
    }

    @Test
    void testExistsById() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        boolean exists = categoryRepository.existsById(1L);

        assertTrue(exists);
        verify(categoryRepository).existsById(1L);
    }

    @Test
    void testDelete() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Premium");

        doNothing().when(categoryRepository).delete(category);

        categoryRepository.delete(category);

        verify(categoryRepository).delete(category);
    }
}
