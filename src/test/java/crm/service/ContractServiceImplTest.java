package crm.service;

import crm.entity.*;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContractServiceImpl Tests")
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContractServiceImpl contractService;

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        customer = Customer.builder().id(1L).name("Test Customer").build();
        user = User.builder().id(1L).username("testuser").build();

        contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .value(new BigDecimal("10000"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    @DisplayName("Test findByName returns correct contract")
    void testFindByName() {
        when(contractRepository.findByName("Test Contract")).thenReturn(contract);

        Contract found = contractService.findByName("Test Contract");

        assertNotNull(found);
        assertEquals("Test Contract", found.getName());
        verify(contractRepository, times(1)).findByName("Test Contract");
    }

    @Test
    @DisplayName("Test listAllContracts returns all contracts")
    void testListAllContracts() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAll()).thenReturn(contracts);

        Iterable<Contract> result = contractService.listAllContracts();

        assertNotNull(result);
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test showContract returns contract by id")
    void testShowContract() {
        when(contractRepository.findById(1L).orElse(null)).thenReturn(contract);

        Contract found = contractService.showContract(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(contractRepository, times(1)).findById(1L).orElse(null);
    }

    @Test
    @DisplayName("Test findAllByValueLessThanEqual returns contracts below value")
    void testFindAllByValueLessThanEqual() {
        BigDecimal value = new BigDecimal("20000");
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(value);
    }

    @Test
    @DisplayName("Test findAllByValueGreaterThanEqual returns contracts above value")
    void testFindAllByValueGreaterThanEqual() {
        BigDecimal value = new BigDecimal("5000");
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueGreaterThanEqual(value);
    }

    @Test
    @DisplayName("Test findAllByBeginDate returns contracts by begin date")
    void testFindAllByBeginDate() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByBeginDate(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDate(date);
    }

    @Test
    @DisplayName("Test findAllByBeginDateBefore returns contracts before date")
    void testFindAllByBeginDateBefore() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateBefore(date);
    }

    @Test
    @DisplayName("Test findAllByBeginDateAfter returns contracts after date")
    void testFindAllByBeginDateAfter() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateAfter(date);
    }

    @Test
    @DisplayName("Test findAllByEndDate returns contracts by end date")
    void testFindAllByEndDate() {
        LocalDate date = LocalDate.of(2024, 12, 31);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDate(date)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByEndDate(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDate(date);
    }

    @Test
    @DisplayName("Test findAllByEndDateBefore returns contracts before end date")
    void testFindAllByEndDateBefore() {
        LocalDate date = LocalDate.of(2025, 12, 31);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDateBefore(date)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByEndDateBefore(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateBefore(date);
    }

    @Test
    @DisplayName("Test findAllByEndDateAfter returns contracts after end date")
    void testFindAllByEndDateAfter() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDateAfter(date)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByEndDateAfter(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateAfter(date);
    }

    @Test
    @DisplayName("Test findAllByStatus returns contracts by status")
    void testFindAllByStatus() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByStatus(Status.PROPOSED);
    }

    @Test
    @DisplayName("Test findAllByCustomer returns contracts by customer")
    void testFindAllByCustomer() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByCustomer(customer)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByCustomer(customer);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomer(customer);
    }

    @Test
    @DisplayName("Test findAllByUser returns contracts by user")
    void testFindAllByUser() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByUser(user)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByUser(user);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByUser(user);
    }

    @Test
    @DisplayName("Test findAllByCustomerAndUser returns contracts by customer and user")
    void testFindAllByCustomerAndUser() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByCustomerAndUser(customer, user);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomerAndUser(customer, user);
    }

    @Test
    @DisplayName("Test saveContract saves contract successfully")
    void testSaveContract() {
        when(contractRepository.save(contract)).thenReturn(contract);

        contractService.saveContract(contract);

        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    @DisplayName("Test findByName returns null when not found")
    void testFindByNameNotFound() {
        when(contractRepository.findByName("Nonexistent")).thenReturn(null);

        Contract found = contractService.findByName("Nonexistent");

        assertNull(found);
    }

    @Test
    @DisplayName("Test showContract returns null when not found")
    void testShowContractNotFound() {
        when(contractRepository.findById(999L).orElse(null)).thenReturn(null);

        Contract found = contractService.showContract(999L);

        assertNull(found);
    }
}
