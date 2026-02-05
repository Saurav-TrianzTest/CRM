package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {

    private Contract contract;

    @BeforeEach
    public void setUp() {
        contract = new Contract();
    }

    @Test
    public void testContractCreation() {
        assertNotNull(contract);
    }

    @Test
    public void testBuilderPattern() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .build();

        assertNotNull(contract);
        assertEquals("Test Contract", contract.getName());
        assertEquals(new BigDecimal("10000.00"), contract.getValue());
    }

    @Test
    public void testSetAndGetId() {
        contract.setId(50L);
        assertEquals(50L, contract.getId());
    }

    @Test
    public void testSetAndGetName() {
        contract.setName("Service Contract");
        assertEquals("Service Contract", contract.getName());
    }

    @Test
    public void testSetAndGetContent() {
        contract.setContent("Contract details");
        assertEquals("Contract details", contract.getContent());
    }

    @Test
    public void testSetAndGetValue() {
        BigDecimal value = new BigDecimal("5000.50");
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    public void testSetAndGetBeginDate() {
        LocalDate date = LocalDate.of(2024, 6, 1);
        contract.setBeginDate(date);
        assertEquals(date, contract.getBeginDate());
    }

    @Test
    public void testSetAndGetEndDate() {
        LocalDate date = LocalDate.of(2025, 6, 1);
        contract.setEndDate(date);
        assertEquals(date, contract.getEndDate());
    }

    @Test
    public void testSetAndGetStatus() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    public void testSetAndGetCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        contract.setCustomer(customer);

        assertNotNull(contract.getCustomer());
        assertEquals(1L, contract.getCustomer().getId());
    }

    @Test
    public void testSetAndGetUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        contract.setUser(user);

        assertNotNull(contract.getUser());
        assertEquals(1L, contract.getUser().getId());
    }

    @Test
    public void testContractWithNullFields() {
        contract.setName(null);
        contract.setContent(null);
        contract.setValue(null);

        assertNull(contract.getName());
        assertNull(contract.getContent());
        assertNull(contract.getValue());
    }

    @Test
    public void testContractEquality() {
        Contract c1 = Contract.builder()
                .id(1L)
                .name("Contract1")
                .value(new BigDecimal("1000"))
                .build();

        Contract c2 = Contract.builder()
                .id(1L)
                .name("Contract1")
                .value(new BigDecimal("1000"))
                .build();

        assertEquals(c1, c2);
    }

    @Test
    public void testContractHashCode() {
        contract.setId(1L);
        contract.setName("Test");
        int hashCode = contract.hashCode();
        assertTrue(hashCode != 0);
    }
}
