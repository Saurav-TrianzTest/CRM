package crm.service;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceImplTest {

    private ContractServiceImpl service;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ContractServiceImpl(contractRepository, customerRepository, userRepository);
    }

    @Test
    void testFindByName() {
        Contract contract = new Contract();
        when(contractRepository.findByName("Test")).thenReturn(contract);

        Contract result = service.findByName("Test");
        assertNotNull(result);
        verify(contractRepository).findByName("Test");
    }

    @Test
    void testListAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAll()).thenReturn(contracts);

        Iterable<Contract> result = service.listAllContracts();
        assertNotNull(result);
        verify(contractRepository).findAll();
    }

    @Test
    void testShowContract() {
        Contract contract = new Contract();
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        Contract result = service.showContract(1L);
        assertNotNull(result);
        verify(contractRepository).findById(1L);
    }

    @Test
    void testFindAllByValueLessThanEqual() {
        BigDecimal value = new BigDecimal("1000");
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(contracts);

        Iterable<Contract> result = service.findAllByValueLessThanEqual(value);
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByValueGreaterThanEqual() {
        BigDecimal value = new BigDecimal("5000");
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(contracts);

        Iterable<Contract> result = service.findAllByValueGreaterThanEqual(value);
        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void testFindAllByBeginDate() {
        LocalDate date = LocalDate.now();
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);

        Iterable<Contract> result = service.findAllByBeginDate(date);
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    void testFindAllByStatus() {
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);

        Iterable<Contract> result = service.findAllByStatus(Status.PROPOSED);
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testFindAllByCustomer() {
        Customer customer = new Customer();
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByCustomer(customer)).thenReturn(contracts);

        Iterable<Contract> result = service.findAllByCustomer(customer);
        assertNotNull(result);
        verify(contractRepository).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByUser() {
        User user = new User();
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByUser(user)).thenReturn(contracts);

        Iterable<Contract> result = service.findAllByUser(user);
        assertNotNull(result);
        verify(contractRepository).findAllByUser(user);
    }
}
