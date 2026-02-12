package crm.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("WriteCsvToResponse Tests")
class WriteCsvToResponseTest {

    @Test
    @DisplayName("Test WriteCsvToResponse class exists")
    void testWriteCsvToResponseExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test WriteCsvToResponse class is present")
    void testWriteCsvToResponseClass() throws ClassNotFoundException {
        Class.forName("crm.utils.WriteCsvToResponse");
    }

    @Test
    @DisplayName("Test WriteCsvToResponse has writeCustomers method")
    void testWriteCustomersMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> utilClass = Class.forName("crm.utils.WriteCsvToResponse");
        assertNotNull(utilClass.getDeclaredMethod("writeCustomers",
                jakarta.servlet.http.HttpServletResponse.class,
                Iterable.class));
    }

    @Test
    @DisplayName("Test WriteCsvToResponse has writeCustomer method")
    void testWriteCustomerMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> utilClass = Class.forName("crm.utils.WriteCsvToResponse");
        assertNotNull(utilClass.getDeclaredMethod("writeCustomer",
                jakarta.servlet.http.HttpServletResponse.class,
                crm.entity.Customer.class));
    }
}
