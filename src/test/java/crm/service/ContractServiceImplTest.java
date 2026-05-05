package crm.service;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceImplTest {

    private ContractServiceImpl contractService;
    private ContractRepository contractRepository;
    private CustomerRepository customerRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        contractRepository = mock(ContractRepository.class);
        customerRepository = mock(CustomerRepository.class);
        userRepository = mock(UserRepository.class);
        contractService = new ContractServiceImpl(contractRepository, customerRepository, userRepository);
    }

    @Test
    void constructor_shouldCreateContractServiceImpl() {
        // Assert
        assertNotNull(contractService);
    }

    @Test
    void findByName_shouldReturnContract() {
        // Arrange
        Contract contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .build();
        
        when(contractRepository.findByName("Test Contract")).thenReturn(contract);
        
        // Act
        Contract result = contractService.findByName("Test Contract");
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
        verify(contractRepository).findByName("Test Contract");
    }

    @Test
    void listAllContracts_shouldReturnAllContracts() {
        // Arrange
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAll()).thenReturn(contracts);
        
        // Act
        Iterable<Contract> result = contractService.listAllContracts();
        
        // Assert
        assertNotNull(result);
        verify(contractRepository).findAll();
    }

    @Test
    void showContract_shouldReturnContract() {
        // Arrange
        Contract contract = Contract.builder()
                .id(1L)
                .name("Test")
                .build();
        
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        
        // Act
        Contract result = contractService.showContract(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(contractRepository).findById(1L);
    }

    @Test
    void findAllByValueLessThanEqual_shouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("1000.00");
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(contracts);
        
        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);
        
        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    void findAllByValueGreaterThanEqual_shouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("5000.00");
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(contracts);
        
        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);
        
        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void findAllByBeginDate_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 1);
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);
        
        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(date);
        
        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    void findAllByStatus_shouldReturnContracts() {
        // Arrange
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);
        
        // Act
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);
        
        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void saveContract_shouldSaveContract() {
        // Arrange
        Contract contract = Contract.builder()
                .name("Test")
                .build();
        
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(contractRepository.save(contract)).thenReturn(contract);
        
        // Act
        contractService.saveContract(contract);
        
        // Assert
        verify(contractRepository).save(contract);
        verify(customerRepository).saveAll(any());
        verify(userRepository).saveAll(any());
    }

    @Test
    void service_shouldHaveServiceAnnotation() {
        // Assert
        assertTrue(ContractServiceImpl.class.isAnnotationPresent(
            org.springframework.stereotype.Service.class));
    }
}
