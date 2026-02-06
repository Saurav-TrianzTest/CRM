package crm.repository;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractRepositoryTest {

    @Mock
    private ContractRepository contractRepository;

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setId(1L);
        contract.setName("Test Contract");
        contract.setValue(new BigDecimal("10000"));
        contract.setBeginDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2024, 12, 31));
        contract.setStatus(Status.PROPOSED);

        customer = new Customer();
        customer.setId(1L);

        user = new User();
        user.setId(1L);
    }

    @Test
    void testFindByName() {
        when(contractRepository.findByName("Test Contract")).thenReturn(contract);

        Contract result = contractRepository.findByName("Test Contract");

        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
        verify(contractRepository).findByName("Test Contract");
    }

    @Test
    void testFindAllByValueLessThanEqual() {
        List<Contract> contracts = Arrays.asList(contract);
        BigDecimal maxValue = new BigDecimal("15000");

        when(contractRepository.findAllByValueLessThanEqual(maxValue)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByValueLessThanEqual(maxValue);

        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(maxValue);
    }

    @Test
    void testFindAllByValueGreaterThanEqual() {
        List<Contract> contracts = Arrays.asList(contract);
        BigDecimal minValue = new BigDecimal("5000");

        when(contractRepository.findAllByValueGreaterThanEqual(minValue)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByValueGreaterThanEqual(minValue);

        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(minValue);
    }

    @Test
    void testFindAllByBeginDate() {
        List<Contract> contracts = Arrays.asList(contract);
        LocalDate date = LocalDate.of(2024, 1, 1);

        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByBeginDate(date);

        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    void testFindAllByBeginDateBefore() {
        List<Contract> contracts = Arrays.asList(contract);
        LocalDate date = LocalDate.of(2024, 6, 1);

        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByBeginDateBefore(date);

        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateBefore(date);
    }

    @Test
    void testFindAllByBeginDateAfter() {
        List<Contract> contracts = Arrays.asList(contract);
        LocalDate date = LocalDate.of(2023, 12, 1);

        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByBeginDateAfter(date);

        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateAfter(date);
    }

    @Test
    void testFindAllByEndDate() {
        List<Contract> contracts = Arrays.asList(contract);
        LocalDate date = LocalDate.of(2024, 12, 31);

        when(contractRepository.findAllByEndDate(date)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByEndDate(date);

        assertNotNull(result);
        verify(contractRepository).findAllByEndDate(date);
    }

    @Test
    void testFindAllByEndDateBefore() {
        List<Contract> contracts = Arrays.asList(contract);
        LocalDate date = LocalDate.of(2025, 1, 1);

        when(contractRepository.findAllByEndDateBefore(date)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByEndDateBefore(date);

        assertNotNull(result);
        verify(contractRepository).findAllByEndDateBefore(date);
    }

    @Test
    void testFindAllByEndDateAfter() {
        List<Contract> contracts = Arrays.asList(contract);
        LocalDate date = LocalDate.of(2024, 6, 1);

        when(contractRepository.findAllByEndDateAfter(date)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByEndDateAfter(date);

        assertNotNull(result);
        verify(contractRepository).findAllByEndDateAfter(date);
    }

    @Test
    void testFindAllByStatus() {
        List<Contract> contracts = Arrays.asList(contract);

        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByStatus(Status.PROPOSED);

        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testFindAllByCustomer() {
        List<Contract> contracts = Arrays.asList(contract);

        when(contractRepository.findAllByCustomer(customer)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByCustomer(customer);

        assertNotNull(result);
        verify(contractRepository).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByCustomerAndUser() {
        List<Contract> contracts = Arrays.asList(contract);

        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByCustomerAndUser(customer, user);

        assertNotNull(result);
        verify(contractRepository).findAllByCustomerAndUser(customer, user);
    }

    @Test
    void testFindAllByUser() {
        List<Contract> contracts = Arrays.asList(contract);

        when(contractRepository.findAllByUser(user)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByUser(user);

        assertNotNull(result);
        verify(contractRepository).findAllByUser(user);
    }

    @Test
    void testSave() {
        when(contractRepository.save(contract)).thenReturn(contract);

        Contract result = contractRepository.save(contract);

        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
        verify(contractRepository).save(contract);
    }

    @Test
    void testFindById() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        Optional<Contract> result = contractRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Contract", result.get().getName());
        verify(contractRepository).findById(1L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(contractRepository).deleteById(1L);

        contractRepository.deleteById(1L);

        verify(contractRepository).deleteById(1L);
    }

    @Test
    void testCount() {
        when(contractRepository.count()).thenReturn(10L);

        long count = contractRepository.count();

        assertEquals(10L, count);
        verify(contractRepository).count();
    }
}
