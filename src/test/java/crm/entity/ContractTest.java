package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Contract Entity Tests")
class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("customer@test.com")
                .build();

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("user@test.com")
                .build();

        contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    @DisplayName("Test Contract builder creates valid instance")
    void testContractBuilder() {
        assertNotNull(contract);
        assertEquals(1L, contract.getId());
        assertEquals("Test Contract", contract.getName());
        assertEquals("Contract content", contract.getContent());
        assertEquals(new BigDecimal("10000.00"), contract.getValue());
        assertEquals(LocalDate.of(2024, 1, 1), contract.getBeginDate());
        assertEquals(LocalDate.of(2024, 12, 31), contract.getEndDate());
        assertEquals(Status.PROPOSED, contract.getStatus());
        assertNotNull(contract.getCustomer());
        assertNotNull(contract.getUser());
    }

    @Test
    @DisplayName("Test Contract no-args constructor")
    void testNoArgsConstructor() {
        Contract emptyContract = new Contract();
        assertNotNull(emptyContract);
        assertNull(emptyContract.getId());
        assertNull(emptyContract.getName());
    }

    @Test
    @DisplayName("Test Contract all-args constructor")
    void testAllArgsConstructor() {
        Contract newContract = new Contract(
                2L,
                "New Contract",
                "New content",
                new BigDecimal("20000.00"),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                Status.NEGOTIATED,
                customer,
                user
        );

        assertNotNull(newContract);
        assertEquals(2L, newContract.getId());
        assertEquals("New Contract", newContract.getName());
        assertEquals(Status.NEGOTIATED, newContract.getStatus());
    }

    @Test
    @DisplayName("Test Contract getters and setters")
    void testGettersAndSetters() {
        Contract testContract = new Contract();
        testContract.setId(3L);
        testContract.setName("Updated Contract");
        testContract.setContent("Updated content");
        testContract.setValue(new BigDecimal("30000.00"));
        testContract.setBeginDate(LocalDate.of(2026, 1, 1));
        testContract.setEndDate(LocalDate.of(2026, 12, 31));
        testContract.setStatus(Status.IMPLEMENTED);
        testContract.setCustomer(customer);
        testContract.setUser(user);

        assertEquals(3L, testContract.getId());
        assertEquals("Updated Contract", testContract.getName());
        assertEquals("Updated content", testContract.getContent());
        assertEquals(new BigDecimal("30000.00"), testContract.getValue());
        assertEquals(LocalDate.of(2026, 1, 1), testContract.getBeginDate());
        assertEquals(LocalDate.of(2026, 12, 31), testContract.getEndDate());
        assertEquals(Status.IMPLEMENTED, testContract.getStatus());
        assertEquals(customer, testContract.getCustomer());
        assertEquals(user, testContract.getUser());
    }

    @Test
    @DisplayName("Test Contract with null values")
    void testContractWithNullValues() {
        Contract nullContract = Contract.builder()
                .name(null)
                .content(null)
                .value(null)
                .status(null)
                .customer(null)
                .user(null)
                .build();

        assertNotNull(nullContract);
        assertNull(nullContract.getName());
        assertNull(nullContract.getContent());
        assertNull(nullContract.getValue());
        assertNull(nullContract.getStatus());
        assertNull(nullContract.getCustomer());
        assertNull(nullContract.getUser());
    }

    @Test
    @DisplayName("Test Contract status transitions")
    void testContractStatusTransitions() {
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
    @DisplayName("Test Contract date validation - end date after begin date")
    void testContractDateValidation() {
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        contract.setBeginDate(beginDate);
        contract.setEndDate(endDate);

        assertTrue(contract.getEndDate().isAfter(contract.getBeginDate()));
    }

    @Test
    @DisplayName("Test Contract with same begin and end date")
    void testContractWithSameDates() {
        LocalDate sameDate = LocalDate.of(2024, 6, 15);
        contract.setBeginDate(sameDate);
        contract.setEndDate(sameDate);

        assertEquals(contract.getBeginDate(), contract.getEndDate());
    }

    @Test
    @DisplayName("Test Contract value with zero")
    void testContractValueWithZero() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }

    @Test
    @DisplayName("Test Contract value with negative amount")
    void testContractValueWithNegative() {
        BigDecimal negativeValue = new BigDecimal("-1000.00");
        contract.setValue(negativeValue);
        assertEquals(negativeValue, contract.getValue());
    }

    @Test
    @DisplayName("Test Contract value with large amount")
    void testContractValueWithLargeAmount() {
        BigDecimal largeValue = new BigDecimal("999999999.99");
        contract.setValue(largeValue);
        assertEquals(largeValue, contract.getValue());
    }

    @Test
    @DisplayName("Test Contract equals and hashCode")
    void testEqualsAndHashCode() {
        Contract contract1 = Contract.builder()
                .id(1L)
                .name("Contract 1")
                .build();

        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Contract 1")
                .build();

        assertEquals(contract1, contract2);
        assertEquals(contract1.hashCode(), contract2.hashCode());
    }

    @Test
    @DisplayName("Test Contract toString")
    void testToString() {
        String contractString = contract.toString();
        assertNotNull(contractString);
        assertTrue(contractString.contains("Test Contract"));
        assertTrue(contractString.contains("PROPOSED"));
    }

    @Test
    @DisplayName("Test Contract customer relationship")
    void testCustomerRelationship() {
        Customer newCustomer = Customer.builder()
                .id(2L)
                .name("New Customer")
                .build();

        contract.setCustomer(newCustomer);
        assertEquals(newCustomer, contract.getCustomer());
        assertEquals(2L, contract.getCustomer().getId());
    }

    @Test
    @DisplayName("Test Contract user relationship")
    void testUserRelationship() {
        User newUser = User.builder()
                .id(2L)
                .username("newuser")
                .build();

        contract.setUser(newUser);
        assertEquals(newUser, contract.getUser());
        assertEquals(2L, contract.getUser().getId());
    }
}
