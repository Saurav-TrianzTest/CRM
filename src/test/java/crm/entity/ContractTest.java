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
        customer = Customer.builder()
                .id(1L)
                .name("TestCompany")
                .email("test@example.com")
                .build();
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("user@example.com")
                .build();
    }

    @Test
    void testContractCreation() {
        assertNotNull(contract);
    }

    @Test
    void testContractBuilder() {
        Contract builtContract = Contract.builder()
                .id(1L)
                .name("TestContract")
                .content("Sample contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();

        assertNotNull(builtContract);
        assertEquals(1L, builtContract.getId());
        assertEquals("TestContract", builtContract.getName());
    }

    @Test
    void testSetAndGetId() {
        contract.setId(1L);
        assertEquals(1L, contract.getId());
    }

    @Test
    void testSetAndGetName() {
        contract.setName("TestContract");
        assertEquals("TestContract", contract.getName());
    }

    @Test
    void testSetAndGetContent() {
        contract.setContent("Sample contract content");
        assertEquals("Sample contract content", contract.getContent());
    }

    @Test
    void testSetAndGetValue() {
        BigDecimal value = new BigDecimal("10000.00");
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate() {
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
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
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer() {
        contract.setCustomer(customer);
        assertNotNull(contract.getCustomer());
        assertEquals(customer, contract.getCustomer());
    }

    @Test
    void testSetAndGetUser() {
        contract.setUser(user);
        assertNotNull(contract.getUser());
        assertEquals(user, contract.getUser());
    }

    @Test
    void testContractWithNullValues() {
        contract.setName(null);
        contract.setContent(null);
        contract.setValue(null);

        assertNull(contract.getName());
        assertNull(contract.getContent());
        assertNull(contract.getValue());
    }

    @Test
    void testContractWithAllStatuses() {
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
    void testContractEquality() {
        Contract contract1 = Contract.builder()
                .id(1L)
                .name("TestContract")
                .value(new BigDecimal("10000.00"))
                .build();

        Contract contract2 = Contract.builder()
                .id(1L)
                .name("TestContract")
                .value(new BigDecimal("10000.00"))
                .build();

        assertEquals(contract1, contract2);
    }

    @Test
    void testAllArgsConstructor() {
        Contract contract = new Contract(1L, "TestContract", "Content",
                new BigDecimal("10000.00"), LocalDate.now(), LocalDate.now().plusMonths(6),
                Status.PROPOSED, customer, user);

        assertNotNull(contract);
        assertEquals("TestContract", contract.getName());
    }

    @Test
    void testNoArgsConstructor() {
        Contract contract = new Contract();
        assertNotNull(contract);
    }

    @Test
    void testContractWithZeroValue() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }

    @Test
    void testContractWithNegativeValue() {
        BigDecimal negativeValue = new BigDecimal("-1000.00");
        contract.setValue(negativeValue);
        assertEquals(negativeValue, contract.getValue());
    }
}
