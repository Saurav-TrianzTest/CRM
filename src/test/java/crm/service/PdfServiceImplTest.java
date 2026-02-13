package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for PdfServiceImpl
 * Tests all public methods, constructors, edge cases, null checks, and boundary conditions
 */
@ExtendWith(MockitoExtension.class)
class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf testPdf;

    @BeforeEach
    void setUp() {
        testPdf = Pdf.builder()
                .id(1L)
                .name("test-document.pdf")
                .content("Test PDF Content")
                .build();
    }

    // Constructor Tests
    @Test
    void testConstructor_WithValidRepository() {
        PdfRepository repository = mock(PdfRepository.class);
        PdfServiceImpl service = new PdfServiceImpl(repository);
        assertNotNull(service);
    }

    @Test
    void testConstructor_WithNullRepository() {
        PdfServiceImpl service = new PdfServiceImpl(null);
        assertNotNull(service);
    }

    // findByName Tests
    @Test
    void testFindByName_WithExistingPdf() {
        // Arrange
        String pdfName = "test-document.pdf";
        when(pdfRepository.findByName(pdfName)).thenReturn(testPdf);

        // Act
        Pdf result = pdfService.findByName(pdfName);

        // Assert
        assertNotNull(result);
        assertEquals(testPdf.getId(), result.getId());
        assertEquals(testPdf.getName(), result.getName());
        assertEquals(testPdf.getContent(), result.getContent());
        verify(pdfRepository, times(1)).findByName(pdfName);
    }

    @Test
    void testFindByName_WithNonExistingPdf() {
        // Arrange
        String pdfName = "non-existing.pdf";
        when(pdfRepository.findByName(pdfName)).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName(pdfName);

        // Assert
        assertNull(result);
        verify(pdfRepository, times(1)).findByName(pdfName);
    }

    @Test
    void testFindByName_WithNullName() {
        // Arrange
        when(pdfRepository.findByName(null)).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName(null);

        // Assert
        assertNull(result);
        verify(pdfRepository, times(1)).findByName(null);
    }

    @Test
    void testFindByName_WithEmptyString() {
        // Arrange
        String emptyName = "";
        when(pdfRepository.findByName(emptyName)).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName(emptyName);

        // Assert
        assertNull(result);
        verify(pdfRepository, times(1)).findByName(emptyName);
    }

    @Test
    void testFindByName_WithWhitespaceString() {
        // Arrange
        String whitespaceName = "   ";
        when(pdfRepository.findByName(whitespaceName)).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName(whitespaceName);

        // Assert
        assertNull(result);
        verify(pdfRepository, times(1)).findByName(whitespaceName);
    }

    @Test
    void testFindByName_WithSpecialCharacters() {
        // Arrange
        String specialName = "test@#$%.pdf";
        Pdf specialPdf = Pdf.builder()
                .id(2L)
                .name(specialName)
                .content("Special Content")
                .build();
        when(pdfRepository.findByName(specialName)).thenReturn(specialPdf);

        // Act
        Pdf result = pdfService.findByName(specialName);

        // Assert
        assertNotNull(result);
        assertEquals(specialName, result.getName());
        verify(pdfRepository, times(1)).findByName(specialName);
    }

    @Test
    void testFindByName_WithVeryLongName() {
        // Arrange
        String longName = "a".repeat(255) + ".pdf";
        Pdf longNamePdf = Pdf.builder()
                .id(3L)
                .name(longName)
                .content("Long Name Content")
                .build();
        when(pdfRepository.findByName(longName)).thenReturn(longNamePdf);

        // Act
        Pdf result = pdfService.findByName(longName);

        // Assert
        assertNotNull(result);
        assertEquals(longName, result.getName());
        verify(pdfRepository, times(1)).findByName(longName);
    }

    // savePdf Tests
    @Test
    void testSavePdf_WithValidPdf() {
        // Arrange
        when(pdfRepository.save(testPdf)).thenReturn(testPdf);

        // Act
        pdfService.savePdf(testPdf);

        // Assert
        verify(pdfRepository, times(1)).save(testPdf);
    }

    @Test
    void testSavePdf_WithNewPdf() {
        // Arrange
        Pdf newPdf = Pdf.builder()
                .name("new-document.pdf")
                .content("New Content")
                .build();
        when(pdfRepository.save(newPdf)).thenReturn(newPdf);

        // Act
        pdfService.savePdf(newPdf);

        // Assert
        verify(pdfRepository, times(1)).save(newPdf);
    }

    @Test
    void testSavePdf_WithNullPdf() {
        // Arrange
        when(pdfRepository.save(null)).thenReturn(null);

        // Act
        pdfService.savePdf(null);

        // Assert
        verify(pdfRepository, times(1)).save(null);
    }

    @Test
    void testSavePdf_WithPdfWithoutId() {
        // Arrange
        Pdf pdfWithoutId = Pdf.builder()
                .name("no-id-document.pdf")
                .content("No ID Content")
                .build();
        when(pdfRepository.save(pdfWithoutId)).thenReturn(pdfWithoutId);

        // Act
        pdfService.savePdf(pdfWithoutId);

        // Assert
        verify(pdfRepository, times(1)).save(pdfWithoutId);
    }

    @Test
    void testSavePdf_WithPdfWithoutContent() {
        // Arrange
        Pdf pdfWithoutContent = Pdf.builder()
                .id(4L)
                .name("no-content.pdf")
                .build();
        when(pdfRepository.save(pdfWithoutContent)).thenReturn(pdfWithoutContent);

        // Act
        pdfService.savePdf(pdfWithoutContent);

        // Assert
        verify(pdfRepository, times(1)).save(pdfWithoutContent);
    }

    @Test
    void testSavePdf_WithPdfWithEmptyName() {
        // Arrange
        Pdf pdfWithEmptyName = Pdf.builder()
                .id(5L)
                .name("")
                .content("Empty Name Content")
                .build();
        when(pdfRepository.save(pdfWithEmptyName)).thenReturn(pdfWithEmptyName);

        // Act
        pdfService.savePdf(pdfWithEmptyName);

        // Assert
        verify(pdfRepository, times(1)).save(pdfWithEmptyName);
    }

    @Test
    void testSavePdf_WithPdfWithNullName() {
        // Arrange
        Pdf pdfWithNullName = Pdf.builder()
                .id(6L)
                .name(null)
                .content("Null Name Content")
                .build();
        when(pdfRepository.save(pdfWithNullName)).thenReturn(pdfWithNullName);

        // Act
        pdfService.savePdf(pdfWithNullName);

        // Assert
        verify(pdfRepository, times(1)).save(pdfWithNullName);
    }

    @Test
    void testSavePdf_MultipleTimes() {
        // Arrange
        when(pdfRepository.save(testPdf)).thenReturn(testPdf);

        // Act
        pdfService.savePdf(testPdf);
        pdfService.savePdf(testPdf);
        pdfService.savePdf(testPdf);

        // Assert
        verify(pdfRepository, times(3)).save(testPdf);
    }

    @Test
    void testSavePdf_WithUpdatedPdf() {
        // Arrange
        testPdf.setContent("Updated Content");
        when(pdfRepository.save(testPdf)).thenReturn(testPdf);

        // Act
        pdfService.savePdf(testPdf);

        // Assert
        verify(pdfRepository, times(1)).save(testPdf);
        assertEquals("Updated Content", testPdf.getContent());
    }

    // Integration Tests
    @Test
    void testFindByNameAndSave_Workflow() {
        // Arrange
        String pdfName = "workflow-test.pdf";
        when(pdfRepository.findByName(pdfName)).thenReturn(null);
        Pdf newPdf = Pdf.builder()
                .name(pdfName)
                .content("Workflow Content")
                .build();
        when(pdfRepository.save(newPdf)).thenReturn(newPdf);

        // Act
        Pdf foundPdf = pdfService.findByName(pdfName);
        assertNull(foundPdf);

        pdfService.savePdf(newPdf);

        when(pdfRepository.findByName(pdfName)).thenReturn(newPdf);
        Pdf foundAfterSave = pdfService.findByName(pdfName);

        // Assert
        assertNotNull(foundAfterSave);
        assertEquals(pdfName, foundAfterSave.getName());
        verify(pdfRepository, times(2)).findByName(pdfName);
        verify(pdfRepository, times(1)).save(newPdf);
    }
}
