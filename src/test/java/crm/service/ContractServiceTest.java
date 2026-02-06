package crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Test
    void testInterfaceExists() {
        assertNotNull(ContractService.class);
    }

    @Test
    void testInterfaceMethods() throws NoSuchMethodException {
        assertNotNull(ContractService.class.getMethod("findByName", String.class));
        assertNotNull(ContractService.class.getMethod("listAllContracts"));
        assertNotNull(ContractService.class.getMethod("showContract", Long.class));
        assertNotNull(ContractService.class.getMethod("saveContract", crm.entity.Contract.class));
    }
}
