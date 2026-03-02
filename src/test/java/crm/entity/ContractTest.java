package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        contract = new Contract();
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(contract);
    }

    @Test
    void testBuilderConstructor() {
        Contract contractWithBuilder = Contract.builder()
                .id(1L)
                .name("Contract A")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();

        assertEquals(1L, contractWithBuilder.getId());
        assertEquals("Contract A", contractWithBuilder.getName());
        assertEquals(Status.PROPOSED, contractWithBuilder.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        Contract contractWithArgs = new Contract(1L, "Contract B", "Content here",
                new BigDecimal("5000"), LocalDate.now(), LocalDate.now().plusMonths(6),
                Status.NEGOTIATED, customer, user);

        assertEquals(1L, contractWithArgs.getId());
        assertEquals("Contract B", contractWithArgs.getName());
        assertEquals(Status.NEGOTIATED, contractWithArgs.getStatus());
    }

    @Test
    void testSetAndGetId() {
        contract.setId(5L);
        assertEquals(5L, contract.getId());
    }

    @Test
    void testSetAndGetName() {
        contract.setName("Important Contract");
        assertEquals("Important Contract", contract.getName());
    }

    @Test
    void testSetAndGetContent() {
        contract.setContent("This is the contract content");
        assertEquals("This is the contract content", contract.getContent());
    }

    @Test
    void testSetAndGetValue() {
        BigDecimal value = new BigDecimal("15000.50");
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate() {
        LocalDate beginDate = LocalDate.of(2024, 3, 1);
        contract.setBeginDate(beginDate);
        assertEquals(beginDate, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate() {
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        contract.setEndDate(endDate);
        assertEquals(endDate, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatus() {
        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer() {
        contract.setCustomer(customer);
        assertEquals(customer, contract.getCustomer());
    }

    @Test
    void testSetAndGetUser() {
        contract.setUser(user);
        assertEquals(user, contract.getUser());
    }

    @Test
    void testAllStatuses() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());

        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());

        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());

        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testValueWithZero() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }
}
