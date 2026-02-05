package crm.csv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CSVTestTest {

    @Test
    public void testCSVTestClassExists() {
        assertDoesNotThrow(() -> {
            CSVTest csvTest = new CSVTest();
            assertNotNull(csvTest);
        });
    }

    @Test
    public void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            // We can't actually test main without GUI, but we can verify it exists
            CSVTest.class.getDeclaredMethod("main", String[].class);
        });
    }

    @Test
    public void testCSVTestInstantiation() {
        CSVTest csvTest = new CSVTest();
        assertNotNull(csvTest);
    }
}
