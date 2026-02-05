package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRepositoryTest {

    @Test
    public void testCustomerRepositoryInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.repository.CustomerRepository"));
    }

    @Test
    public void testCustomerRepositoryHasGetMaxIdMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("getMaxId"));
    }

    @Test
    public void testCustomerRepositoryHasFindAllByEnabledMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("findAllByEnabled", int.class));
    }

    @Test
    public void testCustomerRepositoryHasFindOneByNameMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("findOneByName", String.class));
    }

    @Test
    public void testCustomerRepositoryHasFindByEmailMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("findByEmail", String.class));
    }

    @Test
    public void testCustomerRepositoryHasFindByPhoneMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("findByPhone", int.class));
    }

    @Test
    public void testCustomerRepositoryHasFindByFirstNameMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("findByFirstName", String.class));
    }

    @Test
    public void testCustomerRepositoryHasFindByLastNameMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("findByLastName", String.class));
    }

    @Test
    public void testCustomerRepositoryHasFindByCityMethod() throws NoSuchMethodException {
        assertNotNull(CustomerRepository.class.getMethod("findByCity", String.class));
    }
}
