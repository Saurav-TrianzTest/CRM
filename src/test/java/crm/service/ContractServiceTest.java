package crm.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContractServiceTest {

    @Test
    public void testContractServiceInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.service.ContractService"));
    }

    @Test
    public void testContractServiceHasFindByNameMethod() throws NoSuchMethodException {
        assertNotNull(ContractService.class.getMethod("findByName", String.class));
    }

    @Test
    public void testContractServiceHasListAllContractsMethod() throws NoSuchMethodException {
        assertNotNull(ContractService.class.getMethod("listAllContracts"));
    }

    @Test
    public void testContractServiceHasShowContractMethod() throws NoSuchMethodException {
        assertNotNull(ContractService.class.getMethod("showContract", Long.class));
    }

    @Test
    public void testContractServiceHasSaveContractMethod() throws NoSuchMethodException {
        assertNotNull(ContractService.class.getMethod("saveContract", crm.entity.Contract.class));
    }
}
