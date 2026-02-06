package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testStatusEnumValues() {
        Status[] statuses = Status.values();
        assertNotNull(statuses);
        assertEquals(4, statuses.length);
    }

    @Test
    void testStatusProposed() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
    }

    @Test
    void testStatusNegotiated() {
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
    }

    @Test
    void testStatusImplemented() {
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
    }

    @Test
    void testStatusDone() {
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void testStatusAllArray() {
        assertNotNull(Status.ALL);
        assertEquals(4, Status.ALL.length);
    }

    @Test
    void testStatusAllContainsProposed() {
        boolean containsProposed = false;
        for (Status status : Status.ALL) {
            if (status == Status.PROPOSED) {
                containsProposed = true;
                break;
            }
        }
        assertTrue(containsProposed);
    }

    @Test
    void testStatusAllContainsNegotiated() {
        boolean containsNegotiated = false;
        for (Status status : Status.ALL) {
            if (status == Status.NEGOTIATED) {
                containsNegotiated = true;
                break;
            }
        }
        assertTrue(containsNegotiated);
    }

    @Test
    void testStatusAllContainsImplemented() {
        boolean containsImplemented = false;
        for (Status status : Status.ALL) {
            if (status == Status.IMPLEMENTED) {
                containsImplemented = true;
                break;
            }
        }
        assertTrue(containsImplemented);
    }

    @Test
    void testStatusAllContainsDone() {
        boolean containsDone = false;
        for (Status status : Status.ALL) {
            if (status == Status.DONE) {
                containsDone = true;
                break;
            }
        }
        assertTrue(containsDone);
    }

    @Test
    void testStatusOrder() {
        assertEquals(Status.PROPOSED, Status.ALL[0]);
        assertEquals(Status.NEGOTIATED, Status.ALL[1]);
        assertEquals(Status.IMPLEMENTED, Status.ALL[2]);
        assertEquals(Status.DONE, Status.ALL[3]);
    }

    @Test
    void testStatusToString() {
        assertEquals("PROPOSED", Status.PROPOSED.toString());
        assertEquals("NEGOTIATED", Status.NEGOTIATED.toString());
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.toString());
        assertEquals("DONE", Status.DONE.toString());
    }
}
