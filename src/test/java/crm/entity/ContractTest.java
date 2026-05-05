package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
    void constructor_shouldCreateContract() {
        // Assert
        assertNotNull(contract);
    }

    @Test
    void builder_shouldCreateContractWithAllFields() {
        // Arrange
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        // Act
        Contract built = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(beginDate)
                .endDate(endDate)
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        
        // Assert
        assertNotNull(built);
        assertEquals(1L, built.getId());
        assertEquals("Test Contract", built.getName());
        assertEquals("Contract content", built.getContent());
        assertEquals(new BigDecimal("10000.00"), built.getValue());
        assertEquals(beginDate, built.getBeginDate());
        assertEquals(endDate, built.getEndDate());
        assertEquals(Status.PROPOSED, built.getStatus());
        assertEquals(customer, built.getCustomer());
        assertEquals(user, built.getUser());
    }

    @Test
    void setId_shouldSetId() {
        // Act
        contract.setId(1L);
        
        // Assert
        assertEquals(1L, contract.getId());
    }

    @Test
    void setName_shouldSetName() {
        // Act
        contract.setName("Contract Name");
        
        // Assert
        assertEquals("Contract Name", contract.getName());
    }

    @Test
    void setContent_shouldSetContent() {
        // Act
        contract.setContent("Contract content");
        
        // Assert
        assertEquals("Contract content", contract.getContent());
    }

    @Test
    void setValue_shouldSetValue() {
        // Arrange
        BigDecimal value = new BigDecimal("5000.00");
        
        // Act
        contract.setValue(value);
        
        // Assert
        assertEquals(value, contract.getValue());
    }

    @Test
    void setBeginDate_shouldSetBeginDate() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 1);
        
        // Act
        contract.setBeginDate(date);
        
        // Assert
        assertEquals(date, contract.getBeginDate());
    }

    @Test
    void setEndDate_shouldSetEndDate() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 12, 31);
        
        // Act
        contract.setEndDate(date);
        
        // Assert
        assertEquals(date, contract.getEndDate());
    }

    @Test
    void setStatus_shouldSetStatus() {
        // Act
        contract.setStatus(Status.IMPLEMENTED);
        
        // Assert
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    void setCustomer_shouldSetCustomer() {
        // Act
        contract.setCustomer(customer);
        
        // Assert
        assertEquals(customer, contract.getCustomer());
    }

    @Test
    void setUser_shouldSetUser() {
        // Act
        contract.setUser(user);
        
        // Assert
        assertEquals(user, contract.getUser());
    }

    @Test
    void allArgsConstructor_shouldCreateContractWithAllFields() {
        // Arrange
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        // Act
        Contract contract = new Contract(1L, "Test", "Content", 
            new BigDecimal("1000"), beginDate, endDate, Status.DONE, customer, user);
        
        // Assert
        assertNotNull(contract);
        assertEquals(1L, contract.getId());
        assertEquals("Test", contract.getName());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyContract() {
        // Act
        Contract contract = new Contract();
        
        // Assert
        assertNotNull(contract);
    }
}
