package crm.repository;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("PdfRepository Tests")
class PdfRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PdfRepository pdfRepository;

    @Test
    @DisplayName("Test PdfRepository extends JpaRepository")
    void testPdfRepositoryExtendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(PdfRepository.class));
    }

    @Test
    @DisplayName("Test PdfRepository is not null")
    void testPdfRepositoryNotNull() {
        assertNotNull(pdfRepository);
    }

    @Test
    @DisplayName("Test findByName returns correct PDF")
    void testFindByName() {
        Pdf pdf = Pdf.builder()
                .name("Test PDF")
                .content("Test content")
                .build();
        entityManager.persist(pdf);
        entityManager.flush();

        Pdf found = pdfRepository.findByName("Test PDF");

        assertNotNull(found);
        assertEquals("Test PDF", found.getName());
    }

    @Test
    @DisplayName("Test findByName returns null for non-existent PDF")
    void testFindByNameNotFound() {
        Pdf found = pdfRepository.findByName("Non-existent PDF");
        assertNull(found);
    }

    @Test
    @DisplayName("Test findByName with empty string")
    void testFindByNameEmptyString() {
        Pdf pdf = Pdf.builder()
                .name("")
                .build();
        entityManager.persist(pdf);
        entityManager.flush();

        Pdf found = pdfRepository.findByName("");
        assertNotNull(found);
        assertEquals("", found.getName());
    }

    @Test
    @DisplayName("Test save PDF")
    void testSavePdf() {
        Pdf pdf = Pdf.builder()
                .name("New PDF")
                .content("New content")
                .build();

        Pdf saved = pdfRepository.save(pdf);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("New PDF", saved.getName());
    }

    @Test
    @DisplayName("Test save PDF without content")
    void testSavePdfWithoutContent() {
        Pdf pdf = Pdf.builder()
                .name("No Content PDF")
                .build();

        Pdf saved = pdfRepository.save(pdf);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("No Content PDF", saved.getName());
        assertNull(saved.getContent());
    }

    @Test
    @DisplayName("Test findById returns correct PDF")
    void testFindById() {
        Pdf pdf = Pdf.builder()
                .name("Find By ID PDF")
                .build();
        Pdf saved = entityManager.persist(pdf);
        entityManager.flush();

        Pdf found = pdfRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Find By ID PDF", found.getName());
    }

    @Test
    @DisplayName("Test findById returns empty for non-existent ID")
    void testFindByIdNotFound() {
        Pdf found = pdfRepository.findById(999999L).orElse(null);
        assertNull(found);
    }

    @Test
    @DisplayName("Test findAll returns all PDFs")
    void testFindAll() {
        Pdf pdf1 = Pdf.builder().name("PDF1").build();
        entityManager.persist(pdf1);

        Pdf pdf2 = Pdf.builder().name("PDF2").build();
        entityManager.persist(pdf2);
        entityManager.flush();

        List<Pdf> pdfs = pdfRepository.findAll();

        assertNotNull(pdfs);
        assertTrue(pdfs.size() >= 2);
    }

    @Test
    @DisplayName("Test delete PDF")
    void testDeletePdf() {
        Pdf pdf = Pdf.builder()
                .name("Delete PDF")
                .build();
        Pdf saved = entityManager.persist(pdf);
        entityManager.flush();

        Long pdfId = saved.getId();
        pdfRepository.deleteById(pdfId);

        Pdf deleted = pdfRepository.findById(pdfId).orElse(null);
        assertNull(deleted);
    }

    @Test
    @DisplayName("Test update PDF name")
    void testUpdatePdfName() {
        Pdf pdf = Pdf.builder()
                .name("Original Name")
                .build();
        Pdf saved = entityManager.persist(pdf);
        entityManager.flush();

        saved.setName("Updated Name");
        Pdf updated = pdfRepository.save(saved);

        assertEquals("Updated Name", updated.getName());
    }

    @Test
    @DisplayName("Test count PDFs")
    void testCountPdfs() {
        long initialCount = pdfRepository.count();

        Pdf pdf = Pdf.builder().name("Count PDF").build();
        entityManager.persist(pdf);
        entityManager.flush();

        long newCount = pdfRepository.count();

        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @DisplayName("Test existsById returns true for existing PDF")
    void testExistsByIdTrue() {
        Pdf pdf = Pdf.builder()
                .name("Exists PDF")
                .build();
        Pdf saved = entityManager.persist(pdf);
        entityManager.flush();

        assertTrue(pdfRepository.existsById(saved.getId()));
    }

    @Test
    @DisplayName("Test existsById returns false for non-existent PDF")
    void testExistsByIdFalse() {
        assertFalse(pdfRepository.existsById(999999L));
    }

    @Test
    @DisplayName("Test save PDF with long name")
    void testSavePdfWithLongName() {
        String longName = "A".repeat(255);
        Pdf pdf = Pdf.builder()
                .name(longName)
                .build();

        Pdf saved = pdfRepository.save(pdf);

        assertNotNull(saved);
        assertEquals(longName, saved.getName());
    }

    @Test
    @DisplayName("Test findByName with special characters")
    void testFindByNameWithSpecialCharacters() {
        Pdf pdf = Pdf.builder()
                .name("PDF-2024_v1.0")
                .build();
        entityManager.persist(pdf);
        entityManager.flush();

        Pdf found = pdfRepository.findByName("PDF-2024_v1.0");

        assertNotNull(found);
        assertEquals("PDF-2024_v1.0", found.getName());
    }

    @Test
    @DisplayName("Test save and retrieve PDF with null content")
    void testSaveAndRetrievePdfWithNullContent() {
        Pdf pdf = Pdf.builder()
                .name("Null Content PDF")
                .content(null)
                .build();

        Pdf saved = pdfRepository.save(pdf);
        Pdf retrieved = pdfRepository.findById(saved.getId()).orElse(null);

        assertNotNull(retrieved);
        assertNull(retrieved.getContent());
    }
}
