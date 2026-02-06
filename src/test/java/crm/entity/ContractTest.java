package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    public void setUp() {
        contract = new Contract();
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    public void testContractConstructor() {
        assertNotNull(contract);
    }

    @Test
    public void testContractBuilder() {
        Contract builtContract = Contract.builder()
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
        assertNotNull(builtContract);
        assertEquals("Test Contract", builtContract.getName());
        assertEquals(Status.PROPOSED, builtContract.getStatus());
    }

    @Test
    public void testSetAndGetId() {
        contract.setId(1L);
        assertEquals(1L, contract.getId());
    }

    @Test
    public void testSetAndGetName() {
        contract.setName("Contract ABC");
        assertEquals("Contract ABC", contract.getName());
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
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        contract.setBeginDate(beginDate);
        assertEquals(beginDate, contract.getBeginDate());
    }

    @Test
    public void testSetAndGetEndDate() {
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        contract.setEndDate(endDate);
        assertEquals(endDate, contract.getEndDate());
    }

    @Test
    public void testSetAndGetStatus() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    public void testSetAndGetCustomer() {
        contract.setCustomer(customer);
        assertNotNull(contract.getCustomer());
        assertEquals(1L, contract.getCustomer().getId());
    }

    @Test
    public void testSetAndGetUser() {
        contract.setUser(user);
        assertNotNull(contract.getUser());
        assertEquals(1L, contract.getUser().getId());
    }

    @Test
    public void testContractWithNullValues() {
        contract.setName(null);
        contract.setContent(null);
        contract.setValue(null);
        assertNull(contract.getName());
        assertNull(contract.getContent());
        assertNull(contract.getValue());
    }

    @Test
    public void testContractWithAllStatuses() {
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
    public void testContractValueBoundary() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
        contract.setValue(new BigDecimal("999999999.99"));
        assertNotNull(contract.getValue());
    }

    @Test
    public void testContractAllArgsConstructor() {
        Contract allArgsContract = new Contract(1L, "Contract", "Content", new BigDecimal("1000"),
                LocalDate.now(), LocalDate.now().plusDays(30), Status.PROPOSED, customer, user);
        assertNotNull(allArgsContract);
        assertEquals("Contract", allArgsContract.getName());
    }
}
