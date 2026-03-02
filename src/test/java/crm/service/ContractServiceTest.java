package crm.service;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractServiceTest {

    @Test
    void testContractServiceInterface() {
        assertNotNull(ContractService.class);
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        ContractService.class.getMethod("findByName", String.class);
    }

    @Test
    void testListAllContractsMethodExists() throws NoSuchMethodException {
        ContractService.class.getMethod("listAllContracts");
    }

    @Test
    void testShowContractMethodExists() throws NoSuchMethodException {
        ContractService.class.getMethod("showContract", Long.class);
    }

    @Test
    void testSaveContractMethodExists() throws NoSuchMethodException {
        ContractService.class.getMethod("saveContract", Contract.class);
    }

    @Test
    void testFindAllByStatusMethodExists() throws NoSuchMethodException {
        ContractService.class.getMethod("findAllByStatus", Status.class);
    }

    @Test
    void testFindAllByCustomerMethodExists() throws NoSuchMethodException {
        ContractService.class.getMethod("findAllByCustomer", Customer.class);
    }

    @Test
    void testFindAllByUserMethodExists() throws NoSuchMethodException {
        ContractService.class.getMethod("findAllByUser", User.class);
    }
}
