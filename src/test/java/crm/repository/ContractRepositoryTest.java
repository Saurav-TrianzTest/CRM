package crm.repository;

import crm.entity.Contract;
import crm.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContractRepositoryTest {

    @MockBean
    private ContractRepository contractRepository;

    @Test
    void testFindByName() {
        Contract contract = Contract.builder().id(1L).name("Test Contract").build();

        when(contractRepository.findByName("Test Contract")).thenReturn(contract);

        Contract result = contractRepository.findByName("Test Contract");
        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
    }

    @Test
    void testFindAllByValueLessThanEqual() {
        ArrayList<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByValueLessThanEqual(new BigDecimal("1000"))).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByValueLessThanEqual(new BigDecimal("1000"));
        assertNotNull(result);
    }

    @Test
    void testFindAllByValueGreaterThanEqual() {
        ArrayList<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByValueGreaterThanEqual(new BigDecimal("1000"))).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByValueGreaterThanEqual(new BigDecimal("1000"));
        assertNotNull(result);
    }

    @Test
    void testFindAllByBeginDate() {
        ArrayList<Contract> contracts = new ArrayList<>();
        LocalDate date = LocalDate.now();
        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByBeginDate(date);
        assertNotNull(result);
    }

    @Test
    void testFindAllByStatus() {
        ArrayList<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);

        Iterable<Contract> result = contractRepository.findAllByStatus(Status.PROPOSED);
        assertNotNull(result);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(contractRepository);
    }
}
