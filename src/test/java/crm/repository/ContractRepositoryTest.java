package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContractRepositoryTest {

    @Test
    public void testContractRepositoryInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.repository.ContractRepository"));
    }

    @Test
    public void testContractRepositoryHasFindByNameMethod() throws NoSuchMethodException {
        assertNotNull(ContractRepository.class.getMethod("findByName", String.class));
    }

    @Test
    public void testContractRepositoryHasFindAllByValueLessThanEqualMethod() throws NoSuchMethodException {
        assertNotNull(ContractRepository.class.getMethod("findAllByValueLessThanEqual", java.math.BigDecimal.class));
    }

    @Test
    public void testContractRepositoryHasFindAllByValueGreaterThanEqualMethod() throws NoSuchMethodException {
        assertNotNull(ContractRepository.class.getMethod("findAllByValueGreaterThanEqual", java.math.BigDecimal.class));
    }

    @Test
    public void testContractRepositoryHasFindAllByBeginDateMethod() throws NoSuchMethodException {
        assertNotNull(ContractRepository.class.getMethod("findAllByBeginDate", java.time.LocalDate.class));
    }

    @Test
    public void testContractRepositoryHasFindAllByStatusMethod() throws NoSuchMethodException {
        assertNotNull(ContractRepository.class.getMethod("findAllByStatus", crm.entity.Status.class));
    }

    @Test
    public void testContractRepositoryHasFindAllByCustomerMethod() throws NoSuchMethodException {
        assertNotNull(ContractRepository.class.getMethod("findAllByCustomer", crm.entity.Customer.class));
    }

    @Test
    public void testContractRepositoryHasFindAllByUserMethod() throws NoSuchMethodException {
        assertNotNull(ContractRepository.class.getMethod("findAllByUser", crm.entity.User.class));
    }
}
