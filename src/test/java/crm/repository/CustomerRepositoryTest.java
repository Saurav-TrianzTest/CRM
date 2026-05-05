package crm.repository;

import crm.entity.Category;
import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    @Test
    void customerRepository_shouldExtendJpaRepository() {
        // Assert
        assertTrue(JpaRepository.class.isAssignableFrom(CustomerRepository.class));
    }

    @Test
    void getMaxId_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("getMaxId");
        });
    }

    @Test
    void findAllByEnabled_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findAllByEnabled", int.class);
        });
    }

    @Test
    void findOneByEnabledAndName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findOneByEnabledAndName", int.class, String.class);
        });
    }

    @Test
    void findOneByName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findOneByName", String.class);
        });
    }

    @Test
    void findByEnabledAndEmail_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findByEnabledAndEmail", int.class, String.class);
        });
    }

    @Test
    void findByEmail_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findByEmail", String.class);
        });
    }

    @Test
    void findByEnabledAndCity_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findByEnabledAndCity", int.class, String.class);
        });
    }

    @Test
    void findByCity_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findByCity", String.class);
        });
    }

    @Test
    void findByEnabledAndPhone_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findByEnabledAndPhone", int.class, int.class);
        });
    }

    @Test
    void findByPhone_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findByPhone", int.class);
        });
    }

    @Test
    void findByEnabledAndCategories_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getMethod("findByEnabledAndCategories", int.class, Set.class);
        });
    }

    @Test
    void repository_shouldHaveRepositoryAnnotation() {
        // Assert
        assertTrue(CustomerRepository.class.isAnnotationPresent(
            org.springframework.stereotype.Repository.class));
    }
}
