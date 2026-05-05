package crm.repository;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.*;

class PdfRepositoryTest {

    @Test
    void pdfRepository_shouldExtendJpaRepository() {
        // Assert
        assertTrue(JpaRepository.class.isAssignableFrom(PdfRepository.class));
    }

    @Test
    void findByName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            PdfRepository.class.getMethod("findByName", String.class);
        });
    }

    @Test
    void findByName_shouldReturnPdf() throws NoSuchMethodException {
        // Arrange
        var method = PdfRepository.class.getMethod("findByName", String.class);
        
        // Assert
        assertEquals(Pdf.class, method.getReturnType());
    }

    @Test
    void repository_shouldHaveRepositoryAnnotation() {
        // Assert
        assertTrue(PdfRepository.class.isAnnotationPresent(
            org.springframework.stereotype.Repository.class));
    }
}
