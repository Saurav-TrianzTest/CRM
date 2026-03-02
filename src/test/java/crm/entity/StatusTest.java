package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testStatusValues() {
        Status[] statuses = Status.values();
        assertEquals(4, statuses.length);
    }

    @Test
    void testStatusProposed() {
        Status status = Status.PROPOSED;
        assertEquals("PROPOSED", status.name());
        assertNotNull(status);
    }

    @Test
    void testStatusNegotiated() {
        Status status = Status.NEGOTIATED;
        assertEquals("NEGOTIATED", status.name());
        assertNotNull(status);
    }

    @Test
    void testStatusImplemented() {
        Status status = Status.IMPLEMENTED;
        assertEquals("IMPLEMENTED", status.name());
        assertNotNull(status);
    }

    @Test
    void testStatusDone() {
        Status status = Status.DONE;
        assertEquals("DONE", status.name());
        assertNotNull(status);
    }

    @Test
    void testStatusAllArray() {
        assertEquals(4, Status.ALL.length);
        assertEquals(Status.PROPOSED, Status.ALL[0]);
        assertEquals(Status.NEGOTIATED, Status.ALL[1]);
        assertEquals(Status.IMPLEMENTED, Status.ALL[2]);
        assertEquals(Status.DONE, Status.ALL[3]);
    }

    @Test
    void testStatusValueOf() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void testStatusValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void testStatusOrdinal() {
        assertEquals(0, Status.PROPOSED.ordinal());
        assertEquals(1, Status.NEGOTIATED.ordinal());
        assertEquals(2, Status.IMPLEMENTED.ordinal());
        assertEquals(3, Status.DONE.ordinal());
    }
}
