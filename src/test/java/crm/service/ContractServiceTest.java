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
    void contractService_shouldBeInterface() {
        // Assert
        assertTrue(ContractService.class.isInterface());
    }

    @Test
    void findByName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("findByName", String.class);
        });
    }

    @Test
    void listAllContracts_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("listAllContracts");
        });
    }

    @Test
    void showContract_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("showContract", Long.class);
        });
    }

    @Test
    void findAllByValueLessThanEqual_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("findAllByValueLessThanEqual", BigDecimal.class);
        });
    }

    @Test
    void findAllByValueGreaterThanEqual_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("findAllByValueGreaterThanEqual", BigDecimal.class);
        });
    }

    @Test
    void findAllByBeginDate_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("findAllByBeginDate", LocalDate.class);
        });
    }

    @Test
    void findAllByStatus_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("findAllByStatus", Status.class);
        });
    }

    @Test
    void findAllByCustomer_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("findAllByCustomer", Customer.class);
        });
    }

    @Test
    void findAllByUser_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("findAllByUser", User.class);
        });
    }

    @Test
    void saveContract_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractService.class.getMethod("saveContract", Contract.class);
        });
    }

    @Test
    void findByName_shouldReturnContract() throws NoSuchMethodException {
        // Arrange
        var method = ContractService.class.getMethod("findByName", String.class);
        
        // Assert
        assertEquals(Contract.class, method.getReturnType());
    }
}
