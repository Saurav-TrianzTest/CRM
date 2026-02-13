package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Contract Entity Tests")
class ContractTest {

    @Mock
    private Customer mockCustomer;

    @Mock
    private User mockUser;

    private Contract contract;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contract = new Contract();
    }

    @Test
    @DisplayName("Test default constructor creates non-null instance")
    void testDefaultConstructor() {
        Contract newContract = new Contract();
        assertNotNull(newContract);
    }

    @Test
    @DisplayName("Test all-args constructor with all fields")
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Contract ABC";
        String content = "Contract content details";
        BigDecimal value = new BigDecimal("50000.00");
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        Status status = Status.PROPOSED;

        Contract contract = new Contract(id, name, content, value, beginDate, endDate, status, mockCustomer, mockUser);

        assertNotNull(contract);
        assertEquals(id, contract.getId());
        assertEquals(name, contract.getName());
        assertEquals(content, contract.getContent());
        assertEquals(value, contract.getValue());
        assertEquals(beginDate, contract.getBeginDate());
        assertEquals(endDate, contract.getEndDate());
        assertEquals(status, contract.getStatus());
        assertEquals(mockCustomer, contract.getCustomer());
        assertEquals(mockUser, contract.getUser());
    }

    @Test
    @DisplayName("Test builder pattern with all fields")
    void testBuilderWithAllFields() {
        Long id = 2L;
        String name = "Builder Contract";
        String content = "Built with builder pattern";
        BigDecimal value = new BigDecimal("100000.50");
        LocalDate beginDate = LocalDate.of(2025, 3, 15);
        LocalDate endDate = LocalDate.of(2026, 3, 15);
        Status status = Status.NEGOTIATED;

        Contract contract = Contract.builder()
                .id(id)
                .name(name)
                .content(content)
                .value(value)
                .beginDate(beginDate)
                .endDate(endDate)
                .status(status)
                .customer(mockCustomer)
                .user(mockUser)
                .build();

        assertNotNull(contract);
        assertEquals(id, contract.getId());
        assertEquals(name, contract.getName());
        assertEquals(content, contract.getContent());
        assertEquals(value, contract.getValue());
        assertEquals(beginDate, contract.getBeginDate());
        assertEquals(endDate, contract.getEndDate());
        assertEquals(status, contract.getStatus());
        assertEquals(mockCustomer, contract.getCustomer());
        assertEquals(mockUser, contract.getUser());
    }

    @Test
    @DisplayName("Test builder with minimal fields")
    void testBuilderWithMinimalFields() {
        Contract contract = Contract.builder()
                .name("Minimal Contract")
                .build();

        assertNotNull(contract);
        assertEquals("Minimal Contract", contract.getName());
        assertNull(contract.getId());
        assertNull(contract.getContent());
        assertNull(contract.getValue());
    }

    @Test
    @DisplayName("Test setId and getId")
    void testSetAndGetId() {
        Long id = 10L;
        contract.setId(id);
        assertEquals(id, contract.getId());
    }

    @Test
    @DisplayName("Test setId with null value")
    void testSetIdNull() {
        contract.setId(null);
        assertNull(contract.getId());
    }

    @Test
    @DisplayName("Test setName and getName")
    void testSetAndGetName() {
        String name = "Test Contract Name";
        contract.setName(name);
        assertEquals(name, contract.getName());
    }

    @Test
    @DisplayName("Test setName with null value")
    void testSetNameNull() {
        contract.setName(null);
        assertNull(contract.getName());
    }

    @Test
    @DisplayName("Test setName with empty string")
    void testSetNameEmpty() {
        String name = "";
        contract.setName(name);
        assertEquals(name, contract.getName());
    }

    @Test
    @DisplayName("Test setContent and getContent")
    void testSetAndGetContent() {
        String content = "Detailed contract content with terms and conditions";
        contract.setContent(content);
        assertEquals(content, contract.getContent());
    }

    @Test
    @DisplayName("Test setContent with null value")
    void testSetContentNull() {
        contract.setContent(null);
        assertNull(contract.getContent());
    }

    @Test
    @DisplayName("Test setValue and getValue")
    void testSetAndGetValue() {
        BigDecimal value = new BigDecimal("75000.99");
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    @DisplayName("Test setValue with zero")
    void testSetValueZero() {
        BigDecimal value = BigDecimal.ZERO;
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    @DisplayName("Test setValue with negative value")
    void testSetValueNegative() {
        BigDecimal value = new BigDecimal("-1000.00");
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    @DisplayName("Test setValue with null")
    void testSetValueNull() {
        contract.setValue(null);
        assertNull(contract.getValue());
    }

    @Test
    @DisplayName("Test setBeginDate and getBeginDate")
    void testSetAndGetBeginDate() {
        LocalDate beginDate = LocalDate.of(2024, 6, 1);
        contract.setBeginDate(beginDate);
        assertEquals(beginDate, contract.getBeginDate());
    }

    @Test
    @DisplayName("Test setBeginDate with null")
    void testSetBeginDateNull() {
        contract.setBeginDate(null);
        assertNull(contract.getBeginDate());
    }

    @Test
    @DisplayName("Test setBeginDate with today's date")
    void testSetBeginDateToday() {
        LocalDate today = LocalDate.now();
        contract.setBeginDate(today);
        assertEquals(today, contract.getBeginDate());
    }

    @Test
    @DisplayName("Test setEndDate and getEndDate")
    void testSetAndGetEndDate() {
        LocalDate endDate = LocalDate.of(2025, 6, 1);
        contract.setEndDate(endDate);
        assertEquals(endDate, contract.getEndDate());
    }

    @Test
    @DisplayName("Test setEndDate with null")
    void testSetEndDateNull() {
        contract.setEndDate(null);
        assertNull(contract.getEndDate());
    }

    @Test
    @DisplayName("Test setEndDate before beginDate")
    void testSetEndDateBeforeBeginDate() {
        LocalDate beginDate = LocalDate.of(2025, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 1);
        contract.setBeginDate(beginDate);
        contract.setEndDate(endDate);
        assertEquals(endDate, contract.getEndDate());
        assertTrue(contract.getEndDate().isBefore(contract.getBeginDate()));
    }

    @Test
    @DisplayName("Test setStatus and getStatus with PROPOSED")
    void testSetAndGetStatusProposed() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());
    }

    @Test
    @DisplayName("Test setStatus and getStatus with NEGOTIATED")
    void testSetAndGetStatusNegotiated() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    @DisplayName("Test setStatus and getStatus with IMPLEMENTED")
    void testSetAndGetStatusImplemented() {
        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    @DisplayName("Test setStatus and getStatus with DONE")
    void testSetAndGetStatusDone() {
        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    @DisplayName("Test setStatus with null")
    void testSetStatusNull() {
        contract.setStatus(null);
        assertNull(contract.getStatus());
    }

    @Test
    @DisplayName("Test setCustomer and getCustomer")
    void testSetAndGetCustomer() {
        contract.setCustomer(mockCustomer);
        assertEquals(mockCustomer, contract.getCustomer());
    }

    @Test
    @DisplayName("Test setCustomer with null")
    void testSetCustomerNull() {
        contract.setCustomer(null);
        assertNull(contract.getCustomer());
    }

    @Test
    @DisplayName("Test setUser and getUser")
    void testSetAndGetUser() {
        contract.setUser(mockUser);
        assertEquals(mockUser, contract.getUser());
    }

    @Test
    @DisplayName("Test setUser with null")
    void testSetUserNull() {
        contract.setUser(null);
        assertNull(contract.getUser());
    }

    @Test
    @DisplayName("Test equals with same object")
    void testEqualsWithSameObject() {
        contract.setId(1L);
        contract.setName("Test Contract");
        assertEquals(contract, contract);
    }

    @Test
    @DisplayName("Test equals with equal objects")
    void testEqualsWithEqualObjects() {
        contract.setId(1L);
        contract.setName("Test Contract");

        Contract other = new Contract();
        other.setId(1L);
        other.setName("Test Contract");

        assertEquals(contract, other);
    }

    @Test
    @DisplayName("Test equals with different objects")
    void testEqualsWithDifferentObjects() {
        contract.setId(1L);
        contract.setName("Test Contract");

        Contract other = new Contract();
        other.setId(2L);
        other.setName("Different Contract");

        assertNotEquals(contract, other);
    }

    @Test
    @DisplayName("Test equals with null")
    void testEqualsWithNull() {
        contract.setId(1L);
        assertNotEquals(null, contract);
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        contract.setId(1L);
        contract.setName("Test Contract");

        int hashCode1 = contract.hashCode();
        int hashCode2 = contract.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test hashCode with equal objects")
    void testHashCodeWithEqualObjects() {
        contract.setId(1L);
        contract.setName("Test Contract");

        Contract other = new Contract();
        other.setId(1L);
        other.setName("Test Contract");

        assertEquals(contract.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test toString contains field values")
    void testToString() {
        contract.setId(1L);
        contract.setName("Test Contract");
        contract.setContent("Test Content");

        String toString = contract.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Test Contract"));
        assertTrue(toString.contains("Test Content"));
    }

    @Test
    @DisplayName("Test complete contract lifecycle")
    void testCompleteContractLifecycle() {
        Contract newContract = Contract.builder()
                .name("Complete Contract")
                .content("Full contract details")
                .value(new BigDecimal("150000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(mockCustomer)
                .user(mockUser)
                .build();

        assertNotNull(newContract);
        assertEquals("Complete Contract", newContract.getName());
        assertEquals(Status.PROPOSED, newContract.getStatus());

        newContract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, newContract.getStatus());

        newContract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, newContract.getStatus());

        newContract.setStatus(Status.DONE);
        assertEquals(Status.DONE, newContract.getStatus());
    }

    @Test
    @DisplayName("Test contract with boundary dates")
    void testContractWithBoundaryDates() {
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;

        contract.setBeginDate(minDate);
        contract.setEndDate(maxDate);

        assertEquals(minDate, contract.getBeginDate());
        assertEquals(maxDate, contract.getEndDate());
    }

    @Test
    @DisplayName("Test contract with large value")
    void testContractWithLargeValue() {
        BigDecimal largeValue = new BigDecimal("999999999999.99");
        contract.setValue(largeValue);
        assertEquals(largeValue, contract.getValue());
    }

    @Test
    @DisplayName("Test contract with very long name")
    void testContractWithLongName() {
        String longName = "A".repeat(1000);
        contract.setName(longName);
        assertEquals(longName, contract.getName());
        assertEquals(1000, contract.getName().length());
    }

    @Test
    @DisplayName("Test contract with very long content")
    void testContractWithLongContent() {
        String longContent = "Lorem ipsum ".repeat(500);
        contract.setContent(longContent);
        assertEquals(longContent, contract.getContent());
        assertTrue(contract.getContent().length() > 5000);
    }
}
