package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatusTest {

    @Test
    void testProposedStatus() {
        Status status = Status.PROPOSED;

        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    void testNegotiatedStatus() {
        Status status = Status.NEGOTIATED;

        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    void testImplementedStatus() {
        Status status = Status.IMPLEMENTED;

        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    void testDoneStatus() {
        Status status = Status.DONE;

        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    void testAllStatusesArray() {
        Status[] allStatuses = Status.ALL;

        assertNotNull(allStatuses);
        assertEquals(4, allStatuses.length);
        assertEquals(Status.PROPOSED, allStatuses[0]);
        assertEquals(Status.NEGOTIATED, allStatuses[1]);
        assertEquals(Status.IMPLEMENTED, allStatuses[2]);
        assertEquals(Status.DONE, allStatuses[3]);
    }

    @Test
    void testStatusValuesMethod() {
        Status[] values = Status.values();

        assertNotNull(values);
        assertEquals(4, values.length);
        assertTrue(containsStatus(values, Status.PROPOSED));
        assertTrue(containsStatus(values, Status.NEGOTIATED));
        assertTrue(containsStatus(values, Status.IMPLEMENTED));
        assertTrue(containsStatus(values, Status.DONE));
    }

    @Test
    void testStatusValueOf() {
        Status status = Status.valueOf("PROPOSED");

        assertNotNull(status);
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void testStatusValueOfAllTypes() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void testInvalidStatusValueOf() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID_STATUS");
        });
    }

    @Test
    void testStatusComparison() {
        Status status1 = Status.PROPOSED;
        Status status2 = Status.PROPOSED;

        assertEquals(status1, status2);
        assertSame(status1, status2);
    }

    @Test
    void testStatusOrdering() {
        Status[] values = Status.values();

        assertEquals(Status.PROPOSED, values[0]);
        assertEquals(Status.NEGOTIATED, values[1]);
        assertEquals(Status.IMPLEMENTED, values[2]);
        assertEquals(Status.DONE, values[3]);
    }

    @Test
    void testStatusToString() {
        assertEquals("PROPOSED", Status.PROPOSED.toString());
        assertEquals("NEGOTIATED", Status.NEGOTIATED.toString());
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.toString());
        assertEquals("DONE", Status.DONE.toString());
    }

    @Test
    void testStatusInequality() {
        assertNotEquals(Status.PROPOSED, Status.NEGOTIATED);
        assertNotEquals(Status.IMPLEMENTED, Status.DONE);
    }

    private boolean containsStatus(Status[] array, Status status) {
        for (Status s : array) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}
