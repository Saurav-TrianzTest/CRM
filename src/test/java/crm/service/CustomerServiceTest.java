package crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomerService Interface Tests")
class CustomerServiceTest {

    @Test
    @DisplayName("Test CustomerService is an interface")
    void testCustomerServiceIsInterface() {
        assertTrue(CustomerService.class.isInterface());
    }

    @Test
    @DisplayName("Test CustomerService has methods")
    void testCustomerServiceHasMethods() {
        int methodCount = CustomerService.class.getDeclaredMethods().length;
        assertTrue(methodCount > 0);
    }

    @Test
    @DisplayName("Test CustomerService is public")
    void testCustomerServiceIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(CustomerService.class.getModifiers()));
    }
}
