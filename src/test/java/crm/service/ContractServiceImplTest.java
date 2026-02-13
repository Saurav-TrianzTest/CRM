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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for ContractServiceImpl
 * Tests all public methods, constructors, edge cases, null checks, and boundary conditions
 */
@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContractServiceImpl contractService;

    private Contract testContract;
    private Customer testCustomer;
    private User testUser;
    private LocalDate testBeginDate;
    private LocalDate testEndDate;

    @BeforeEach
    void setUp() {
        testBeginDate = LocalDate.of(2024, 1, 1);
        testEndDate = LocalDate.of(2024, 12, 31);

        testCustomer = Customer.builder()
                .id(1L)
                .name("TestCustomer")
                .email("customer@test.com")
                .enabled(1)
                .build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("user@test.com")
                .enabled(1)
                .build();

        testContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Test contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(testBeginDate)
                .endDate(testEndDate)
                .status(Status.PROPOSED)
                .customer(testCustomer)
                .user(testUser)
                .build();
    }

    // Constructor Tests
    @Test
    void testConstructor_WithValidRepositories() {
        ContractRepository contractRepo = mock(ContractRepository.class);
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        UserRepository userRepo = mock(UserRepository.class);
        ContractServiceImpl service = new ContractServiceImpl(contractRepo, customerRepo, userRepo);
        assertNotNull(service);
    }

    @Test
    void testConstructor_WithNullRepositories() {
        ContractServiceImpl service = new ContractServiceImpl(null, null, null);
        assertNotNull(service);
    }

    // findByName Tests
    @Test
    void testFindByName_WithExistingContract() {
        // Arrange
        String contractName = "Test Contract";
        when(contractRepository.findByName(contractName)).thenReturn(testContract);

        // Act
        Contract result = contractService.findByName(contractName);

        // Assert
        assertNotNull(result);
        assertEquals(contractName, result.getName());
        assertEquals(testContract.getId(), result.getId());
        verify(contractRepository, times(1)).findByName(contractName);
    }

    @Test
    void testFindByName_WithNonExistingContract() {
        // Arrange
        String contractName = "Non Existing";
        when(contractRepository.findByName(contractName)).thenReturn(null);

        // Act
        Contract result = contractService.findByName(contractName);

        // Assert
        assertNull(result);
        verify(contractRepository, times(1)).findByName(contractName);
    }

    @Test
    void testFindByName_WithNullName() {
        // Arrange
        when(contractRepository.findByName(null)).thenReturn(null);

        // Act
        Contract result = contractService.findByName(null);

        // Assert
        assertNull(result);
        verify(contractRepository, times(1)).findByName(null);
    }

    @Test
    void testFindByName_WithEmptyString() {
        // Arrange
        String emptyName = "";
        when(contractRepository.findByName(emptyName)).thenReturn(null);

        // Act
        Contract result = contractService.findByName(emptyName);

        // Assert
        assertNull(result);
        verify(contractRepository, times(1)).findByName(emptyName);
    }

    @Test
    void testFindByName_WithSpecialCharacters() {
        // Arrange
        String specialName = "Contract@#$%2024";
        Contract specialContract = Contract.builder()
                .id(2L)
                .name(specialName)
                .build();
        when(contractRepository.findByName(specialName)).thenReturn(specialContract);

        // Act
        Contract result = contractService.findByName(specialName);

        // Assert
        assertNotNull(result);
        assertEquals(specialName, result.getName());
        verify(contractRepository, times(1)).findByName(specialName);
    }

    // listAllContracts Tests
    @Test
    void testListAllContracts_WithMultipleContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(testContract,
                Contract.builder().id(2L).name("Contract 2").build());
        when(contractRepository.findAll()).thenReturn(contracts);

        // Act
        Iterable<Contract> result = contractService.listAllContracts();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testListAllContracts_WithEmptyList() {
        // Arrange
        when(contractRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.listAllContracts();

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testListAllContracts_WithSingleContract() {
        // Arrange
        List<Contract> contracts = Collections.singletonList(testContract);
        when(contractRepository.findAll()).thenReturn(contracts);

        // Act
        Iterable<Contract> result = contractService.listAllContracts();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAll();
    }

    // showContract Tests
    @Test
    void testShowContract_WithExistingContract() {
        // Arrange
        Long contractId = 1L;
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(testContract));

        // Act
        Contract result = contractService.showContract(contractId);

        // Assert
        assertNotNull(result);
        assertEquals(contractId, result.getId());
        verify(contractRepository, times(1)).findById(contractId);
    }

    @Test
    void testShowContract_WithNonExistingContract() {
        // Arrange
        Long contractId = 999L;
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // Act
        Contract result = contractService.showContract(contractId);

        // Assert
        assertNull(result);
        verify(contractRepository, times(1)).findById(contractId);
    }

    @Test
    void testShowContract_WithNullId() {
        // Arrange
        when(contractRepository.findById(null)).thenReturn(Optional.empty());

        // Act
        Contract result = contractService.showContract(null);

        // Assert
        assertNull(result);
        verify(contractRepository, times(1)).findById(null);
    }

    @Test
    void testShowContract_WithZeroId() {
        // Arrange
        Long zeroId = 0L;
        when(contractRepository.findById(zeroId)).thenReturn(Optional.empty());

        // Act
        Contract result = contractService.showContract(zeroId);

        // Assert
        assertNull(result);
        verify(contractRepository, times(1)).findById(zeroId);
    }

    // findAllByValueLessThanEqual Tests
    @Test
    void testFindAllByValueLessThanEqual_WithExistingContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("15000.00");
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByValueLessThanEqual_WithZeroValue() {
        // Arrange
        BigDecimal value = BigDecimal.ZERO;
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByValueLessThanEqual_WithNullValue() {
        // Arrange
        when(contractRepository.findAllByValueLessThanEqual(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(null);
    }

    @Test
    void testFindAllByValueLessThanEqual_WithNegativeValue() {
        // Arrange
        BigDecimal value = new BigDecimal("-1000.00");
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(value);
    }

    // findAllByValueGreaterThanEqual Tests
    @Test
    void testFindAllByValueGreaterThanEqual_WithExistingContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("5000.00");
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void testFindAllByValueGreaterThanEqual_WithZeroValue() {
        // Arrange
        BigDecimal value = BigDecimal.ZERO;
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void testFindAllByValueGreaterThanEqual_WithNullValue() {
        // Arrange
        when(contractRepository.findAllByValueGreaterThanEqual(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueGreaterThanEqual(null);
    }

    // findAllByBeginDate Tests
    @Test
    void testFindAllByBeginDate_WithExistingContracts() {
        // Arrange
        when(contractRepository.findAllByBeginDate(testBeginDate)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(testBeginDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByBeginDate(testBeginDate);
    }

    @Test
    void testFindAllByBeginDate_WithNullDate() {
        // Arrange
        when(contractRepository.findAllByBeginDate(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDate(null);
    }

    @Test
    void testFindAllByBeginDate_WithFutureDate() {
        // Arrange
        LocalDate futureDate = LocalDate.of(2025, 12, 31);
        when(contractRepository.findAllByBeginDate(futureDate)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(futureDate);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDate(futureDate);
    }

    @Test
    void testFindAllByBeginDate_WithPastDate() {
        // Arrange
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        when(contractRepository.findAllByBeginDate(pastDate)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(pastDate);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDate(pastDate);
    }

    // findAllByBeginDateBefore Tests
    @Test
    void testFindAllByBeginDateBefore_WithExistingContracts() {
        // Arrange
        LocalDate beforeDate = LocalDate.of(2024, 6, 1);
        when(contractRepository.findAllByBeginDateBefore(beforeDate)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(beforeDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByBeginDateBefore(beforeDate);
    }

    @Test
    void testFindAllByBeginDateBefore_WithNullDate() {
        // Arrange
        when(contractRepository.findAllByBeginDateBefore(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateBefore(null);
    }

    // findAllByBeginDateAfter Tests
    @Test
    void testFindAllByBeginDateAfter_WithExistingContracts() {
        // Arrange
        LocalDate afterDate = LocalDate.of(2023, 12, 31);
        when(contractRepository.findAllByBeginDateAfter(afterDate)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(afterDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByBeginDateAfter(afterDate);
    }

    @Test
    void testFindAllByBeginDateAfter_WithNullDate() {
        // Arrange
        when(contractRepository.findAllByBeginDateAfter(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateAfter(null);
    }

    // findAllByEndDate Tests
    @Test
    void testFindAllByEndDate_WithExistingContracts() {
        // Arrange
        when(contractRepository.findAllByEndDate(testEndDate)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByEndDate(testEndDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByEndDate(testEndDate);
    }

    @Test
    void testFindAllByEndDate_WithNullDate() {
        // Arrange
        when(contractRepository.findAllByEndDate(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByEndDate(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDate(null);
    }

    // findAllByEndDateBefore Tests
    @Test
    void testFindAllByEndDateBefore_WithExistingContracts() {
        // Arrange
        LocalDate beforeDate = LocalDate.of(2025, 1, 1);
        when(contractRepository.findAllByEndDateBefore(beforeDate)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByEndDateBefore(beforeDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByEndDateBefore(beforeDate);
    }

    @Test
    void testFindAllByEndDateBefore_WithNullDate() {
        // Arrange
        when(contractRepository.findAllByEndDateBefore(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByEndDateBefore(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateBefore(null);
    }

    // findAllByEndDateAfter Tests
    @Test
    void testFindAllByEndDateAfter_WithExistingContracts() {
        // Arrange
        LocalDate afterDate = LocalDate.of(2024, 6, 1);
        when(contractRepository.findAllByEndDateAfter(afterDate)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByEndDateAfter(afterDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByEndDateAfter(afterDate);
    }

    @Test
    void testFindAllByEndDateAfter_WithNullDate() {
        // Arrange
        when(contractRepository.findAllByEndDateAfter(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByEndDateAfter(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateAfter(null);
    }

    // findAllByStatus Tests
    @Test
    void testFindAllByStatus_WithActiveStatus() {
        // Arrange
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testFindAllByStatus_WithNullStatus() {
        // Arrange
        when(contractRepository.findAllByStatus(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByStatus(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByStatus(null);
    }

    @Test
    void testFindAllByStatus_WithInactiveStatus() {
        // Arrange
        when(contractRepository.findAllByStatus(Status.DONE)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByStatus(Status.DONE);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByStatus(Status.DONE);
    }

    // findAllByCustomer Tests
    @Test
    void testFindAllByCustomer_WithExistingContracts() {
        // Arrange
        when(contractRepository.findAllByCustomer(testCustomer)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByCustomer(testCustomer);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByCustomer(testCustomer);
    }

    @Test
    void testFindAllByCustomer_WithNullCustomer() {
        // Arrange
        when(contractRepository.findAllByCustomer(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByCustomer(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomer(null);
    }

    @Test
    void testFindAllByCustomer_WithCustomerWithNoContracts() {
        // Arrange
        Customer emptyCustomer = Customer.builder()
                .id(999L)
                .name("EmptyCustomer")
                .build();
        when(contractRepository.findAllByCustomer(emptyCustomer)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByCustomer(emptyCustomer);

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(contractRepository, times(1)).findAllByCustomer(emptyCustomer);
    }

    // findAllByCustomerAndUser Tests
    @Test
    void testFindAllByCustomerAndUser_WithExistingContracts() {
        // Arrange
        when(contractRepository.findAllByCustomerAndUser(testCustomer, testUser))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(testCustomer, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByCustomerAndUser(testCustomer, testUser);
    }

    @Test
    void testFindAllByCustomerAndUser_WithNullCustomer() {
        // Arrange
        when(contractRepository.findAllByCustomerAndUser(null, testUser))
                .thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(null, testUser);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomerAndUser(null, testUser);
    }

    @Test
    void testFindAllByCustomerAndUser_WithNullUser() {
        // Arrange
        when(contractRepository.findAllByCustomerAndUser(testCustomer, null))
                .thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(testCustomer, null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomerAndUser(testCustomer, null);
    }

    @Test
    void testFindAllByCustomerAndUser_WithBothNull() {
        // Arrange
        when(contractRepository.findAllByCustomerAndUser(null, null))
                .thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(null, null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomerAndUser(null, null);
    }

    // findAllByUser Tests
    @Test
    void testFindAllByUser_WithExistingContracts() {
        // Arrange
        when(contractRepository.findAllByUser(testUser)).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByUser(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Contract>) result).size());
        verify(contractRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    void testFindAllByUser_WithNullUser() {
        // Arrange
        when(contractRepository.findAllByUser(null)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByUser(null);

        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByUser(null);
    }

    @Test
    void testFindAllByUser_WithUserWithNoContracts() {
        // Arrange
        User emptyUser = User.builder()
                .id(999L)
                .username("emptyuser")
                .build();
        when(contractRepository.findAllByUser(emptyUser)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Contract> result = contractService.findAllByUser(emptyUser);

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(contractRepository, times(1)).findAllByUser(emptyUser);
    }

    // saveContract Tests
    @Test
    void testSaveContract_WithValidContract() {
        // Arrange
        when(contractRepository.save(testContract)).thenReturn(testContract);

        // Act
        contractService.saveContract(testContract);

        // Assert
        verify(contractRepository, times(1)).save(testContract);
    }

    @Test
    void testSaveContract_WithNewContract() {
        // Arrange
        Contract newContract = Contract.builder()
                .name("New Contract")
                .content("New content")
                .value(new BigDecimal("5000.00"))
                .build();
        when(contractRepository.save(newContract)).thenReturn(newContract);

        // Act
        contractService.saveContract(newContract);

        // Assert
        verify(contractRepository, times(1)).save(newContract);
    }

    @Test
    void testSaveContract_WithNullContract() {
        // Arrange
        when(contractRepository.save(null)).thenReturn(null);

        // Act
        contractService.saveContract(null);

        // Assert
        verify(contractRepository, times(1)).save(null);
    }

    @Test
    void testSaveContract_WithContractWithoutId() {
        // Arrange
        Contract contractWithoutId = Contract.builder()
                .name("No ID Contract")
                .value(new BigDecimal("1000.00"))
                .build();
        when(contractRepository.save(contractWithoutId)).thenReturn(contractWithoutId);

        // Act
        contractService.saveContract(contractWithoutId);

        // Assert
        verify(contractRepository, times(1)).save(contractWithoutId);
    }

    @Test
    void testSaveContract_WithContractWithoutCustomer() {
        // Arrange
        Contract contractWithoutCustomer = Contract.builder()
                .id(3L)
                .name("No Customer Contract")
                .value(new BigDecimal("2000.00"))
                .build();
        when(contractRepository.save(contractWithoutCustomer)).thenReturn(contractWithoutCustomer);

        // Act
        contractService.saveContract(contractWithoutCustomer);

        // Assert
        verify(contractRepository, times(1)).save(contractWithoutCustomer);
    }

    @Test
    void testSaveContract_WithContractWithoutUser() {
        // Arrange
        Contract contractWithoutUser = Contract.builder()
                .id(4L)
                .name("No User Contract")
                .customer(testCustomer)
                .value(new BigDecimal("3000.00"))
                .build();
        when(contractRepository.save(contractWithoutUser)).thenReturn(contractWithoutUser);

        // Act
        contractService.saveContract(contractWithoutUser);

        // Assert
        verify(contractRepository, times(1)).save(contractWithoutUser);
    }

    @Test
    void testSaveContract_WithMinimalContract() {
        // Arrange
        Contract minimalContract = Contract.builder()
                .name("Minimal Contract")
                .build();
        when(contractRepository.save(minimalContract)).thenReturn(minimalContract);

        // Act
        contractService.saveContract(minimalContract);

        // Assert
        verify(contractRepository, times(1)).save(minimalContract);
    }

    @Test
    void testSaveContract_WithUpdatedContract() {
        // Arrange
        testContract.setContent("Updated content");
        testContract.setValue(new BigDecimal("20000.00"));
        when(contractRepository.save(testContract)).thenReturn(testContract);

        // Act
        contractService.saveContract(testContract);

        // Assert
        verify(contractRepository, times(1)).save(testContract);
        assertEquals("Updated content", testContract.getContent());
        assertEquals(new BigDecimal("20000.00"), testContract.getValue());
    }

    @Test
    void testSaveContract_MultipleTimes() {
        // Arrange
        when(contractRepository.save(testContract)).thenReturn(testContract);

        // Act
        contractService.saveContract(testContract);
        contractService.saveContract(testContract);
        contractService.saveContract(testContract);

        // Assert
        verify(contractRepository, times(3)).save(testContract);
    }

    // Integration Tests
    @Test
    void testFindByNameAndSave_Workflow() {
        // Arrange
        String contractName = "Workflow Contract";
        when(contractRepository.findByName(contractName)).thenReturn(null);
        Contract newContract = Contract.builder()
                .name(contractName)
                .value(new BigDecimal("7500.00"))
                .build();
        when(contractRepository.save(newContract)).thenReturn(newContract);

        // Act
        Contract foundContract = contractService.findByName(contractName);
        assertNull(foundContract);

        contractService.saveContract(newContract);

        when(contractRepository.findByName(contractName)).thenReturn(newContract);
        Contract foundAfterSave = contractService.findByName(contractName);

        // Assert
        assertNotNull(foundAfterSave);
        assertEquals(contractName, foundAfterSave.getName());
        verify(contractRepository, times(2)).findByName(contractName);
        verify(contractRepository, times(1)).save(newContract);
    }
}
