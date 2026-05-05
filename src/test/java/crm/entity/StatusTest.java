package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void values_shouldReturnAllStatuses() {
        // Act
        Status[] statuses = Status.values();
        
        // Assert
        assertNotNull(statuses);
        assertEquals(4, statuses.length);
    }

    @Test
    void valueOf_shouldReturnCorrectStatus() {
        // Act
        Status status = Status.valueOf("PROPOSED");
        
        // Assert
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void all_shouldContainAllStatuses() {
        // Assert
        assertNotNull(Status.ALL);
        assertEquals(4, Status.ALL.length);
        assertEquals(Status.PROPOSED, Status.ALL[0]);
        assertEquals(Status.NEGOTIATED, Status.ALL[1]);
        assertEquals(Status.IMPLEMENTED, Status.ALL[2]);
        assertEquals(Status.DONE, Status.ALL[3]);
    }

    @Test
    void proposed_shouldExist() {
        // Assert
        assertNotNull(Status.PROPOSED);
        assertEquals("PROPOSED", Status.PROPOSED.name());
    }

    @Test
    void negotiated_shouldExist() {
        // Assert
        assertNotNull(Status.NEGOTIATED);
        assertEquals("NEGOTIATED", Status.NEGOTIATED.name());
    }

    @Test
    void implemented_shouldExist() {
        // Assert
        assertNotNull(Status.IMPLEMENTED);
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.name());
    }

    @Test
    void done_shouldExist() {
        // Assert
        assertNotNull(Status.DONE);
        assertEquals("DONE", Status.DONE.name());
    }

    @Test
    void valueOf_withInvalidValue_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID");
        });
    }
}
