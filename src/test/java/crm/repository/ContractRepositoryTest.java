package crm.repository;

import crm.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("ContractRepository Tests")
class ContractRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContractRepository contractRepository;

    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        user = User.builder()
                .username("testuser")
                .email("user@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        entityManager.persist(user);

        customer = Customer.builder()
                .name("Test Customer")
                .email("customer@example.com")
                .phone(123456789)
                .enabled(1)
                .build();
        entityManager.persist(customer);
        entityManager.flush();
    }

    @Test
    @DisplayName("Test ContractRepository extends JpaRepository")
    void testContractRepositoryExtendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(ContractRepository.class));
    }

    @Test
    @DisplayName("Test ContractRepository is not null")
    void testContractRepositoryNotNull() {
        assertNotNull(contractRepository);
    }

    @Test
    @DisplayName("Test findByName returns correct contract")
    void testFindByName() {
        Contract contract = Contract.builder()
                .name("Test Contract")
                .content("Content")
                .value(new BigDecimal("10000"))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Contract found = contractRepository.findByName("Test Contract");

        assertNotNull(found);
        assertEquals("Test Contract", found.getName());
    }

    @Test
    @DisplayName("Test findAllByValueLessThanEqual returns contracts below value")
    void testFindAllByValueLessThanEqual() {
        Contract contract1 = Contract.builder()
                .name("Contract1")
                .value(new BigDecimal("5000"))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract1);

        Contract contract2 = Contract.builder()
                .name("Contract2")
                .value(new BigDecimal("15000"))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract2);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByValueLessThanEqual(new BigDecimal("10000"));
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
        assertTrue(contractList.stream().allMatch(c -> c.getValue().compareTo(new BigDecimal("10000")) <= 0));
    }

    @Test
    @DisplayName("Test findAllByValueGreaterThanEqual returns contracts above value")
    void testFindAllByValueGreaterThanEqual() {
        Contract contract1 = Contract.builder()
                .name("Contract1")
                .value(new BigDecimal("5000"))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract1);

        Contract contract2 = Contract.builder()
                .name("Contract2")
                .value(new BigDecimal("15000"))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract2);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByValueGreaterThanEqual(new BigDecimal("10000"));
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
        assertTrue(contractList.stream().allMatch(c -> c.getValue().compareTo(new BigDecimal("10000")) >= 0));
    }

    @Test
    @DisplayName("Test findAllByBeginDate returns contracts with specific begin date")
    void testFindAllByBeginDate() {
        LocalDate date = LocalDate.of(2024, 1, 1);

        Contract contract = Contract.builder()
                .name("Contract1")
                .beginDate(date)
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByBeginDate(date);
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
        assertTrue(contractList.stream().allMatch(c -> c.getBeginDate().equals(date)));
    }

    @Test
    @DisplayName("Test findAllByBeginDateBefore returns contracts before date")
    void testFindAllByBeginDateBefore() {
        Contract contract = Contract.builder()
                .name("Contract1")
                .beginDate(LocalDate.of(2023, 1, 1))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByBeginDateBefore(LocalDate.of(2024, 1, 1));
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
    }

    @Test
    @DisplayName("Test findAllByBeginDateAfter returns contracts after date")
    void testFindAllByBeginDateAfter() {
        Contract contract = Contract.builder()
                .name("Contract1")
                .beginDate(LocalDate.of(2025, 1, 1))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByBeginDateAfter(LocalDate.of(2024, 1, 1));
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
    }

    @Test
    @DisplayName("Test findAllByEndDate returns contracts with specific end date")
    void testFindAllByEndDate() {
        LocalDate date = LocalDate.of(2024, 12, 31);

        Contract contract = Contract.builder()
                .name("Contract1")
                .endDate(date)
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByEndDate(date);
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
    }

    @Test
    @DisplayName("Test findAllByStatus returns contracts with specific status")
    void testFindAllByStatus() {
        Contract contract1 = Contract.builder()
                .name("Contract1")
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract1);

        Contract contract2 = Contract.builder()
                .name("Contract2")
                .status(Status.DONE)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract2);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByStatus(Status.PROPOSED);
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
        assertTrue(contractList.stream().allMatch(c -> c.getStatus() == Status.PROPOSED));
    }

    @Test
    @DisplayName("Test findAllByCustomer returns contracts for customer")
    void testFindAllByCustomer() {
        Contract contract = Contract.builder()
                .name("Contract1")
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByCustomer(customer);
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
        assertTrue(contractList.stream().allMatch(c -> c.getCustomer().equals(customer)));
    }

    @Test
    @DisplayName("Test findAllByUser returns contracts for user")
    void testFindAllByUser() {
        Contract contract = Contract.builder()
                .name("Contract1")
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByUser(user);
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
        assertTrue(contractList.stream().allMatch(c -> c.getUser().equals(user)));
    }

    @Test
    @DisplayName("Test findAllByCustomerAndUser returns contracts for customer and user")
    void testFindAllByCustomerAndUser() {
        Contract contract = Contract.builder()
                .name("Contract1")
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        entityManager.persist(contract);
        entityManager.flush();

        Iterable<Contract> contracts = contractRepository.findAllByCustomerAndUser(customer, user);
        List<Contract> contractList = new ArrayList<>();
        contracts.forEach(contractList::add);

        assertFalse(contractList.isEmpty());
        assertTrue(contractList.stream().allMatch(c ->
            c.getCustomer().equals(customer) && c.getUser().equals(user)));
    }

    @Test
    @DisplayName("Test save contract")
    void testSaveContract() {
        Contract contract = Contract.builder()
                .name("New Contract")
                .content("Content")
                .value(new BigDecimal("20000"))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();

        Contract saved = contractRepository.save(contract);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("New Contract", saved.getName());
    }

    @Test
    @DisplayName("Test delete contract")
    void testDeleteContract() {
        Contract contract = Contract.builder()
                .name("Delete Contract")
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        Contract saved = entityManager.persist(contract);
        entityManager.flush();

        Long contractId = saved.getId();
        contractRepository.deleteById(contractId);

        Contract deleted = contractRepository.findById(contractId).orElse(null);
        assertNull(deleted);
    }
}
