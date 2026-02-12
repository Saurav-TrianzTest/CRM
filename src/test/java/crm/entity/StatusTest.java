package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Status Enum Tests")
class StatusTest {

    @Test
    @DisplayName("Test Status enum values exist")
    void testStatusEnumValues() {
        Status[] values = Status.values();
        assertNotNull(values);
        assertEquals(4, values.length);
    }

    @Test
    @DisplayName("Test Status PROPOSED exists")
    void testStatusProposed() {
        Status status = Status.PROPOSED;
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    @DisplayName("Test Status NEGOTIATED exists")
    void testStatusNegotiated() {
        Status status = Status.NEGOTIATED;
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    @DisplayName("Test Status IMPLEMENTED exists")
    void testStatusImplemented() {
        Status status = Status.IMPLEMENTED;
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    @DisplayName("Test Status DONE exists")
    void testStatusDone() {
        Status status = Status.DONE;
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    @DisplayName("Test Status ALL constant array")
    void testStatusAllConstant() {
        Status[] all = Status.ALL;
        assertNotNull(all);
        assertEquals(4, all.length);
    }

    @Test
    @DisplayName("Test Status ALL contains PROPOSED")
    void testStatusAllContainsProposed() {
        Status[] all = Status.ALL;
        boolean containsProposed = false;
        for (Status status : all) {
            if (status == Status.PROPOSED) {
                containsProposed = true;
                break;
            }
        }
        assertTrue(containsProposed);
    }

    @Test
    @DisplayName("Test Status ALL contains NEGOTIATED")
    void testStatusAllContainsNegotiated() {
        Status[] all = Status.ALL;
        boolean containsNegotiated = false;
        for (Status status : all) {
            if (status == Status.NEGOTIATED) {
                containsNegotiated = true;
                break;
            }
        }
        assertTrue(containsNegotiated);
    }

    @Test
    @DisplayName("Test Status ALL contains IMPLEMENTED")
    void testStatusAllContainsImplemented() {
        Status[] all = Status.ALL;
        boolean containsImplemented = false;
        for (Status status : all) {
            if (status == Status.IMPLEMENTED) {
                containsImplemented = true;
                break;
            }
        }
        assertTrue(containsImplemented);
    }

    @Test
    @DisplayName("Test Status ALL contains DONE")
    void testStatusAllContainsDone() {
        Status[] all = Status.ALL;
        boolean containsDone = false;
        for (Status status : all) {
            if (status == Status.DONE) {
                containsDone = true;
                break;
            }
        }
        assertTrue(containsDone);
    }

    @Test
    @DisplayName("Test Status valueOf with valid string")
    void testStatusValueOf() {
        Status status = Status.valueOf("PROPOSED");
        assertEquals(Status.PROPOSED, status);

        status = Status.valueOf("NEGOTIATED");
        assertEquals(Status.NEGOTIATED, status);

        status = Status.valueOf("IMPLEMENTED");
        assertEquals(Status.IMPLEMENTED, status);

        status = Status.valueOf("DONE");
        assertEquals(Status.DONE, status);
    }

    @Test
    @DisplayName("Test Status valueOf with invalid string throws exception")
    void testStatusValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID");
        });
    }

    @Test
    @DisplayName("Test Status valueOf with null throws exception")
    void testStatusValueOfNull() {
        assertThrows(NullPointerException.class, () -> {
            Status.valueOf(null);
        });
    }

    @Test
    @DisplayName("Test Status valueOf with lowercase throws exception")
    void testStatusValueOfLowercase() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("proposed");
        });
    }

    @Test
    @DisplayName("Test Status ordinal values")
    void testStatusOrdinals() {
        assertEquals(0, Status.PROPOSED.ordinal());
        assertEquals(1, Status.NEGOTIATED.ordinal());
        assertEquals(2, Status.IMPLEMENTED.ordinal());
        assertEquals(3, Status.DONE.ordinal());
    }

    @Test
    @DisplayName("Test Status equality")
    void testStatusEquality() {
        Status status1 = Status.PROPOSED;
        Status status2 = Status.PROPOSED;
        assertEquals(status1, status2);
        assertSame(status1, status2);
    }

    @Test
    @DisplayName("Test Status inequality")
    void testStatusInequality() {
        Status status1 = Status.PROPOSED;
        Status status2 = Status.DONE;
        assertNotEquals(status1, status2);
    }

    @Test
    @DisplayName("Test Status switch statement")
    void testStatusSwitchStatement() {
        Status status = Status.PROPOSED;
        String result = switch (status) {
            case PROPOSED -> "Proposed";
            case NEGOTIATED -> "Negotiated";
            case IMPLEMENTED -> "Implemented";
            case DONE -> "Done";
        };
        assertEquals("Proposed", result);
    }

    @Test
    @DisplayName("Test Status ALL array is not modifiable reference")
    void testStatusAllArrayReference() {
        Status[] all1 = Status.ALL;
        Status[] all2 = Status.ALL;
        assertSame(all1, all2);
    }

    @Test
    @DisplayName("Test Status name method")
    void testStatusNameMethod() {
        assertEquals("PROPOSED", Status.PROPOSED.name());
        assertEquals("NEGOTIATED", Status.NEGOTIATED.name());
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.name());
        assertEquals("DONE", Status.DONE.name());
    }

    @Test
    @DisplayName("Test Status toString method")
    void testStatusToStringMethod() {
        assertEquals("PROPOSED", Status.PROPOSED.toString());
        assertEquals("NEGOTIATED", Status.NEGOTIATED.toString());
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.toString());
        assertEquals("DONE", Status.DONE.toString());
    }
}
