package crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContractService Interface Tests")
class ContractServiceTest {

    @Test
    @DisplayName("Test ContractService is an interface")
    void testContractServiceIsInterface() {
        assertTrue(ContractService.class.isInterface());
    }

    @Test
    @DisplayName("Test ContractService has methods")
    void testContractServiceHasMethods() {
        int methodCount = ContractService.class.getDeclaredMethods().length;
        assertTrue(methodCount > 0);
    }

    @Test
    @DisplayName("Test ContractService is public")
    void testContractServiceIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ContractService.class.getModifiers()));
    }
}
