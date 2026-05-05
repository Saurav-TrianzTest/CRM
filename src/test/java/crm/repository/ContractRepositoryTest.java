package crm.repository;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractRepositoryTest {

    @Test
    void contractRepository_shouldExtendJpaRepository() {
        // Assert
        assertTrue(JpaRepository.class.isAssignableFrom(ContractRepository.class));
    }

    @Test
    void findByName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findByName", String.class);
        });
    }

    @Test
    void findAllByValueLessThanEqual_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByValueLessThanEqual", BigDecimal.class);
        });
    }

    @Test
    void findAllByValueGreaterThanEqual_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByValueGreaterThanEqual", BigDecimal.class);
        });
    }

    @Test
    void findAllByBeginDate_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByBeginDate", LocalDate.class);
        });
    }

    @Test
    void findAllByBeginDateBefore_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByBeginDateBefore", LocalDate.class);
        });
    }

    @Test
    void findAllByBeginDateAfter_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByBeginDateAfter", LocalDate.class);
        });
    }

    @Test
    void findAllByEndDate_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByEndDate", LocalDate.class);
        });
    }

    @Test
    void findAllByStatus_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByStatus", Status.class);
        });
    }

    @Test
    void findAllByCustomer_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByCustomer", Customer.class);
        });
    }

    @Test
    void findAllByUser_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            ContractRepository.class.getMethod("findAllByUser", User.class);
        });
    }

    @Test
    void repository_shouldHaveRepositoryAnnotation() {
        // Assert
        assertTrue(ContractRepository.class.isAnnotationPresent(
            org.springframework.stereotype.Repository.class));
    }
}
