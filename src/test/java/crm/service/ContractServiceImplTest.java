package crm.service;

import crm.entity.Contract;
import crm.entity.Status;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContractServiceImpl contractService;

    private Contract testContract;

    @BeforeEach
    public void setUp() {
        testContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .value(new BigDecimal("10000"))
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(6))
                .status(Status.PROPOSED)
                .build();
    }

    @Test
    public void testFindByName() {
        when(contractRepository.findByName("Test Contract")).thenReturn(testContract);
        Contract result = contractService.findByName("Test Contract");
        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
    }

    @Test
    public void testListAllContracts() {
        when(contractRepository.findAll()).thenReturn(new ArrayList<>());
        Iterable<Contract> result = contractService.listAllContracts();
        assertNotNull(result);
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    public void testShowContract() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(testContract));
        Contract result = contractService.showContract(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindAllByValueLessThanEqual() {
        when(contractRepository.findAllByValueLessThanEqual(any(BigDecimal.class))).thenReturn(new ArrayList<>());
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(new BigDecimal("5000"));
        assertNotNull(result);
    }

    @Test
    public void testFindAllByStatus() {
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(new ArrayList<>());
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);
        assertNotNull(result);
    }

    @Test
    public void testSaveContract() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        
        contractService.saveContract(testContract);
        verify(contractRepository, times(1)).save(testContract);
    }
}
