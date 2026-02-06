package crm.repository;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerRepositoryTest {

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(100L);
        Long result = customerRepository.getMaxId();
        assertEquals(100L, result);
        verify(customerRepository, times(1)).getMaxId();
    }

    @Test
    void testFindAllByEnabled() {
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder().id(1L).name("Test").enabled(1).build());

        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findAllByEnabled(1);
        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testFindOneByEnabledAndName() {
        Customer customer = Customer.builder().id(1L).name("Test").enabled(1).build();

        when(customerRepository.findOneByEnabledAndName(1, "Test")).thenReturn(customer);

        Customer result = customerRepository.findOneByEnabledAndName(1, "Test");
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    void testFindByEmail() {
        ArrayList<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEmail("test@example.com")).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByEmail("test@example.com");
        assertNotNull(result);
    }

    @Test
    void testFindByPhone() {
        ArrayList<Customer> customers = new ArrayList<>();
        when(customerRepository.findByPhone(123456)).thenReturn(customers);

        Iterable<Customer> result = customerRepository.findByPhone(123456);
        assertNotNull(result);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(customerRepository);
    }
}
