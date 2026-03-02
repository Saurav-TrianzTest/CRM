package crm.repository;

import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractRepositoryTest {

    @Test
    void testContractRepositoryInterface() {
        assertNotNull(ContractRepository.class);
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        ContractRepository.class.getMethod("findByName", String.class);
    }

    @Test
    void testFindAllByValueLessThanEqualMethodExists() throws NoSuchMethodException {
        ContractRepository.class.getMethod("findAllByValueLessThanEqual", BigDecimal.class);
    }

    @Test
    void testFindAllByValueGreaterThanEqualMethodExists() throws NoSuchMethodException {
        ContractRepository.class.getMethod("findAllByValueGreaterThanEqual", BigDecimal.class);
    }

    @Test
    void testFindAllByBeginDateMethodExists() throws NoSuchMethodException {
        ContractRepository.class.getMethod("findAllByBeginDate", LocalDate.class);
    }

    @Test
    void testFindAllByStatusMethodExists() throws NoSuchMethodException {
        ContractRepository.class.getMethod("findAllByStatus", Status.class);
    }

    @Test
    void testFindAllByCustomerMethodExists() throws NoSuchMethodException {
        ContractRepository.class.getMethod("findAllByCustomer", Customer.class);
    }

    @Test
    void testFindAllByUserMethodExists() throws NoSuchMethodException {
        ContractRepository.class.getMethod("findAllByUser", User.class);
    }
}
