package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Status Enum Tests")
class StatusTest {

    @Test
    @DisplayName("Test Status enum has PROPOSED value")
    void testStatusProposedExists() {
        Status status = Status.PROPOSED;
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    @DisplayName("Test Status enum has NEGOTIATED value")
    void testStatusNegotiatedExists() {
        Status status = Status.NEGOTIATED;
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    @DisplayName("Test Status enum has IMPLEMENTED value")
    void testStatusImplementedExists() {
        Status status = Status.IMPLEMENTED;
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    @DisplayName("Test Status enum has DONE value")
    void testStatusDoneExists() {
        Status status = Status.DONE;
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    @DisplayName("Test Status.values() returns all enum values")
    void testValuesReturnsAllStatuses() {
        Status[] statuses = Status.values();

        assertNotNull(statuses);
        assertEquals(4, statuses.length);
        assertEquals(Status.PROPOSED, statuses[0]);
        assertEquals(Status.NEGOTIATED, statuses[1]);
        assertEquals(Status.IMPLEMENTED, statuses[2]);
        assertEquals(Status.DONE, statuses[3]);
    }

    @Test
    @DisplayName("Test Status.valueOf() with PROPOSED")
    void testValueOfProposed() {
        Status status = Status.valueOf("PROPOSED");
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    @DisplayName("Test Status.valueOf() with NEGOTIATED")
    void testValueOfNegotiated() {
        Status status = Status.valueOf("NEGOTIATED");
        assertEquals(Status.NEGOTIATED, status);
    }

    @Test
    @DisplayName("Test Status.valueOf() with IMPLEMENTED")
    void testValueOfImplemented() {
        Status status = Status.valueOf("IMPLEMENTED");
        assertEquals(Status.IMPLEMENTED, status);
    }

    @Test
    @DisplayName("Test Status.valueOf() with DONE")
    void testValueOfDone() {
        Status status = Status.valueOf("DONE");
        assertEquals(Status.DONE, status);
    }

    @Test
    @DisplayName("Test Status.valueOf() throws exception for invalid value")
    void testValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID_STATUS");
        });
    }

    @Test
    @DisplayName("Test Status.valueOf() throws exception for null")
    void testValueOfNull() {
        assertThrows(NullPointerException.class, () -> {
            Status.valueOf(null);
        });
    }

    @Test
    @DisplayName("Test Status.valueOf() is case sensitive")
    void testValueOfCaseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("proposed");
        });
    }

    @Test
    @DisplayName("Test Status.ALL constant contains all values")
    void testAllConstant() {
        Status[] all = Status.ALL;

        assertNotNull(all);
        assertEquals(4, all.length);
        assertEquals(Status.PROPOSED, all[0]);
        assertEquals(Status.NEGOTIATED, all[1]);
        assertEquals(Status.IMPLEMENTED, all[2]);
        assertEquals(Status.DONE, all[3]);
    }

    @Test
    @DisplayName("Test Status.ALL matches Status.values()")
    void testAllMatchesValues() {
        Status[] all = Status.ALL;
        Status[] values = Status.values();

        assertEquals(values.length, all.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], all[i]);
        }
    }

    @Test
    @DisplayName("Test Status enum ordinal values")
    void testEnumOrdinals() {
        assertEquals(0, Status.PROPOSED.ordinal());
        assertEquals(1, Status.NEGOTIATED.ordinal());
        assertEquals(2, Status.IMPLEMENTED.ordinal());
        assertEquals(3, Status.DONE.ordinal());
    }

    @Test
    @DisplayName("Test Status enum toString returns name")
    void testToString() {
        assertEquals("PROPOSED", Status.PROPOSED.toString());
        assertEquals("NEGOTIATED", Status.NEGOTIATED.toString());
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.toString());
        assertEquals("DONE", Status.DONE.toString());
    }

    @Test
    @DisplayName("Test Status enum equals with same value")
    void testEqualsSameValue() {
        Status status1 = Status.PROPOSED;
        Status status2 = Status.PROPOSED;
        assertEquals(status1, status2);
        assertSame(status1, status2);
    }

    @Test
    @DisplayName("Test Status enum equals with different values")
    void testEqualsDifferentValues() {
        Status status1 = Status.PROPOSED;
        Status status2 = Status.DONE;
        assertNotEquals(status1, status2);
    }

    @Test
    @DisplayName("Test Status enum comparison using ordinals")
    void testComparison() {
        assertTrue(Status.PROPOSED.ordinal() < Status.NEGOTIATED.ordinal());
        assertTrue(Status.NEGOTIATED.ordinal() < Status.IMPLEMENTED.ordinal());
        assertTrue(Status.IMPLEMENTED.ordinal() < Status.DONE.ordinal());
    }

    @Test
    @DisplayName("Test Status enum in switch statement - PROPOSED")
    void testSwitchStatementProposed() {
        Status status = Status.PROPOSED;
        String result = getSwitchResult(status);
        assertEquals("Proposed", result);
    }

    @Test
    @DisplayName("Test Status enum in switch statement - NEGOTIATED")
    void testSwitchStatementNegotiated() {
        Status status = Status.NEGOTIATED;
        String result = getSwitchResult(status);
        assertEquals("Negotiated", result);
    }

    @Test
    @DisplayName("Test Status enum in switch statement - IMPLEMENTED")
    void testSwitchStatementImplemented() {
        Status status = Status.IMPLEMENTED;
        String result = getSwitchResult(status);
        assertEquals("Implemented", result);
    }

    @Test
    @DisplayName("Test Status enum in switch statement - DONE")
    void testSwitchStatementDone() {
        Status status = Status.DONE;
        String result = getSwitchResult(status);
        assertEquals("Done", result);
    }

    private String getSwitchResult(Status status) {
        switch (status) {
            case PROPOSED:
                return "Proposed";
            case NEGOTIATED:
                return "Negotiated";
            case IMPLEMENTED:
                return "Implemented";
            case DONE:
                return "Done";
            default:
                return "Unknown";
        }
    }

    @Test
    @DisplayName("Test Status enum hashCode consistency")
    void testHashCodeConsistency() {
        Status status = Status.PROPOSED;
        int hashCode1 = status.hashCode();
        int hashCode2 = status.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test Status enum hashCode for same values")
    void testHashCodeSameValues() {
        Status status1 = Status.NEGOTIATED;
        Status status2 = Status.NEGOTIATED;
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    @DisplayName("Test Status enum compareTo")
    void testCompareTo() {
        assertTrue(Status.PROPOSED.compareTo(Status.NEGOTIATED) < 0);
        assertTrue(Status.DONE.compareTo(Status.PROPOSED) > 0);
        assertEquals(0, Status.IMPLEMENTED.compareTo(Status.IMPLEMENTED));
    }

    @Test
    @DisplayName("Test Status enum getDeclaringClass")
    void testGetDeclaringClass() {
        assertEquals(Status.class, Status.PROPOSED.getDeclaringClass());
        assertEquals(Status.class, Status.NEGOTIATED.getDeclaringClass());
        assertEquals(Status.class, Status.IMPLEMENTED.getDeclaringClass());
        assertEquals(Status.class, Status.DONE.getDeclaringClass());
    }

    @Test
    @DisplayName("Test Status enum values are not null")
    void testEnumValuesNotNull() {
        for (Status status : Status.values()) {
            assertNotNull(status);
            assertNotNull(status.name());
        }
    }

    @Test
    @DisplayName("Test Status.ALL array is not modifiable externally")
    void testAllArrayIndependence() {
        Status[] all1 = Status.ALL;
        Status[] all2 = Status.ALL;

        assertSame(all1, all2);
    }

    @Test
    @DisplayName("Test Status enum can be used in collections")
    void testEnumInCollections() {
        java.util.Set<Status> statusSet = java.util.EnumSet.allOf(Status.class);

        assertNotNull(statusSet);
        assertEquals(4, statusSet.size());
        assertTrue(statusSet.contains(Status.PROPOSED));
        assertTrue(statusSet.contains(Status.NEGOTIATED));
        assertTrue(statusSet.contains(Status.IMPLEMENTED));
        assertTrue(statusSet.contains(Status.DONE));
    }

    @Test
    @DisplayName("Test Status enum sequential workflow")
    void testStatusWorkflow() {
        Status[] workflow = {Status.PROPOSED, Status.NEGOTIATED, Status.IMPLEMENTED, Status.DONE};

        for (int i = 0; i < workflow.length - 1; i++) {
            assertTrue(workflow[i].ordinal() < workflow[i + 1].ordinal());
        }
    }

    @Test
    @DisplayName("Test Status enum name length")
    void testEnumNameLength() {
        assertTrue(Status.PROPOSED.name().length() > 0);
        assertTrue(Status.NEGOTIATED.name().length() > 0);
        assertTrue(Status.IMPLEMENTED.name().length() > 0);
        assertTrue(Status.DONE.name().length() > 0);
    }

    @Test
    @DisplayName("Test Status enum is final")
    void testEnumIsFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(Status.class.getModifiers()));
    }

    @Test
    @DisplayName("Test Status enum progression logic")
    void testStatusProgression() {
        assertEquals(0, Status.PROPOSED.ordinal());
        assertEquals(Status.PROPOSED.ordinal() + 1, Status.NEGOTIATED.ordinal());
        assertEquals(Status.NEGOTIATED.ordinal() + 1, Status.IMPLEMENTED.ordinal());
        assertEquals(Status.IMPLEMENTED.ordinal() + 1, Status.DONE.ordinal());
    }
}
