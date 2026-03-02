package crm.repository;

import crm.entity.Category;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    @Test
    void testCustomerRepositoryInterface() {
        assertNotNull(CustomerRepository.class);
    }

    @Test
    void testGetMaxIdMethodExists() throws NoSuchMethodException {
        CustomerRepository.class.getMethod("getMaxId");
    }

    @Test
    void testFindAllByEnabledMethodExists() throws NoSuchMethodException {
        CustomerRepository.class.getMethod("findAllByEnabled", int.class);
    }

    @Test
    void testFindOneByEnabledAndNameMethodExists() throws NoSuchMethodException {
        CustomerRepository.class.getMethod("findOneByEnabledAndName", int.class, String.class);
    }

    @Test
    void testFindOneByNameMethodExists() throws NoSuchMethodException {
        CustomerRepository.class.getMethod("findOneByName", String.class);
    }

    @Test
    void testFindByEnabledAndEmailMethodExists() throws NoSuchMethodException {
        CustomerRepository.class.getMethod("findByEnabledAndEmail", int.class, String.class);
    }

    @Test
    void testFindByEmailMethodExists() throws NoSuchMethodException {
        CustomerRepository.class.getMethod("findByEmail", String.class);
    }

    @Test
    void testFindByEnabledAndPhoneMethodExists() throws NoSuchMethodException {
        CustomerRepository.class.getMethod("findByEnabledAndPhone", int.class, int.class);
    }
}
