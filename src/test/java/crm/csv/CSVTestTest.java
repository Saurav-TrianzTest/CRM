package crm.csv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVTestTest {

    @Test
    void testCSVTestClassExists() {
        assertNotNull(CSVTest.class);
    }

    @Test
    void testMainMethodExists() {
        try {
            CSVTest.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("main method should exist");
        }
    }

    @Test
    void testCSVTestInstantiation() {
        CSVTest csvTest = new CSVTest();
        assertNotNull(csvTest);
    }
}
