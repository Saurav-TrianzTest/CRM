package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusTest {

    @Test
    public void testStatusEnumValues() {
        Status[] statuses = Status.values();
        assertNotNull(statuses);
        assertEquals(4, statuses.length);
    }

    @Test
    public void testStatusProposed() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
    }

    @Test
    public void testStatusNegotiated() {
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
    }

    @Test
    public void testStatusImplemented() {
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
    }

    @Test
    public void testStatusDone() {
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    public void testStatusAllConstant() {
        assertNotNull(Status.ALL);
        assertEquals(4, Status.ALL.length);
    }

    @Test
    public void testStatusAllContainsProposed() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.PROPOSED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testStatusAllContainsNegotiated() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.NEGOTIATED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testStatusAllContainsImplemented() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.IMPLEMENTED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testStatusAllContainsDone() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.DONE) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testStatusComparison() {
        assertNotEquals(Status.PROPOSED, Status.DONE);
        assertEquals(Status.PROPOSED, Status.PROPOSED);
    }

    @Test
    public void testInvalidStatusValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID");
        });
    }
}
