package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContractTest {

    private Contract contract;

    @BeforeEach
    void setUp() {
        contract = new Contract();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(contract);
    }

    @Test
    void testBuilderPattern() {
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        Contract builtContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(beginDate)
                .endDate(endDate)
                .status(Status.PROPOSED)
                .build();

        assertNotNull(builtContract);
        assertEquals(1L, builtContract.getId());
        assertEquals("Test Contract", builtContract.getName());
        assertEquals("Contract content", builtContract.getContent());
        assertEquals(new BigDecimal("10000.00"), builtContract.getValue());
        assertEquals(beginDate, builtContract.getBeginDate());
        assertEquals(endDate, builtContract.getEndDate());
        assertEquals(Status.PROPOSED, builtContract.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        Customer customer = new Customer();
        User user = new User();

        Contract contract = new Contract(1L, "Contract Name", "Content",
                new BigDecimal("5000"), beginDate, endDate, Status.PROPOSED, customer, user);

        assertEquals(1L, contract.getId());
        assertEquals("Contract Name", contract.getName());
        assertEquals("Content", contract.getContent());
        assertEquals(new BigDecimal("5000"), contract.getValue());
        assertEquals(beginDate, contract.getBeginDate());
        assertEquals(endDate, contract.getEndDate());
        assertEquals(Status.PROPOSED, contract.getStatus());
        assertEquals(customer, contract.getCustomer());
        assertEquals(user, contract.getUser());
    }

    @Test
    void testGettersAndSetters() {
        Long id = 1L;
        String name = "Annual Contract";
        String content = "Contract details";
        BigDecimal value = new BigDecimal("25000.50");
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        Status status = Status.PROPOSED;

        contract.setId(id);
        contract.setName(name);
        contract.setContent(content);
        contract.setValue(value);
        contract.setBeginDate(beginDate);
        contract.setEndDate(endDate);
        contract.setStatus(status);

        assertEquals(id, contract.getId());
        assertEquals(name, contract.getName());
        assertEquals(content, contract.getContent());
        assertEquals(value, contract.getValue());
        assertEquals(beginDate, contract.getBeginDate());
        assertEquals(endDate, contract.getEndDate());
        assertEquals(status, contract.getStatus());
    }

    @Test
    void testCustomerRelationship() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        contract.setCustomer(customer);

        assertNotNull(contract.getCustomer());
        assertEquals(customer, contract.getCustomer());
        assertEquals(1L, contract.getCustomer().getId());
    }

    @Test
    void testUserRelationship() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        contract.setUser(user);

        assertNotNull(contract.getUser());
        assertEquals(user, contract.getUser());
        assertEquals(1L, contract.getUser().getId());
    }

    @Test
    void testNullValues() {
        contract.setId(null);
        contract.setName(null);
        contract.setContent(null);
        contract.setValue(null);
        contract.setBeginDate(null);
        contract.setEndDate(null);
        contract.setStatus(null);
        contract.setCustomer(null);
        contract.setUser(null);

        assertNull(contract.getId());
        assertNull(contract.getName());
        assertNull(contract.getContent());
        assertNull(contract.getValue());
        assertNull(contract.getBeginDate());
        assertNull(contract.getEndDate());
        assertNull(contract.getStatus());
        assertNull(contract.getCustomer());
        assertNull(contract.getUser());
    }

    @Test
    void testDifferentStatuses() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());

        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());

        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testValuePrecision() {
        BigDecimal value = new BigDecimal("12345.67");
        contract.setValue(value);

        assertEquals(value, contract.getValue());
        assertEquals(0, value.compareTo(contract.getValue()));
    }

    @Test
    void testDateComparison() {
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        contract.setBeginDate(beginDate);
        contract.setEndDate(endDate);

        assertTrue(contract.getBeginDate().isBefore(contract.getEndDate()));
    }

    @Test
    void testEquality() {
        Contract contract1 = Contract.builder()
                .id(1L)
                .name("Contract")
                .build();

        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Contract")
                .build();

        assertEquals(contract1, contract2);
    }

    @Test
    void testToString() {
        contract.setId(1L);
        contract.setName("Test Contract");

        String toString = contract.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Test Contract"));
    }
}
