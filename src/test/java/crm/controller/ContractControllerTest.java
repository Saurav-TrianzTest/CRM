package crm.controller;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.service.ContractService;
import crm.service.CustomerService;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ContractController contractController;

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);

        user = new User();
        user.setId(1L);

        contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .value(new BigDecimal("10000"))
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    public void testShowAllContracts() {
        when(contractService.listAllContracts()).thenReturn(new ArrayList<>());
        String result = contractController.showAllContracts(model);
        assertEquals("contract/list", result);
        verify(contractService, times(1)).listAllContracts();
    }

    @Test
    public void testShowFormAddContract() {
        when(customerService.findAllByEnabledTrue()).thenReturn(new ArrayList<>());
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        String result = contractController.showFormAddContract(model);
        assertEquals("contract/add", result);
    }

    @Test
    public void testProcessRequestAddContractWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = contractController.processRequestAddContract(contract, bindingResult);
        assertEquals("redirect:/contract/add", result);
    }

    @Test
    public void testProcessRequestAddContractSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = contractController.processRequestAddContract(contract, bindingResult);
        assertEquals("contract/success", result);
        verify(contractService, times(1)).saveContract(contract);
    }

    @Test
    public void testShowFormEditContract() {
        when(contractService.showContract(1L)).thenReturn(contract);
        String result = contractController.showFormEditContract(model, 1L);
        assertEquals("contract/edit", result);
    }

    @Test
    public void testProcessRequestEditContractSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = contractController.processRequestEditContract(1L, contract, bindingResult);
        assertEquals("redirect:/contract/list", result);
        verify(contractService, times(1)).saveContract(contract);
    }

    @Test
    public void testShowNameSearchForm() {
        String result = contractController.showNameSearchForm(model);
        assertEquals("contract/name-search", result);
    }

    @Test
    public void testProcessRequestNameSearch() {
        when(contractService.findByName("Test Contract")).thenReturn(contract);
        String result = contractController.processRequestNameSearch(contract, model);
        assertEquals("contract/show-one", result);
    }

    @Test
    public void testShowValueLessThanEqualSearchForm() {
        String result = contractController.showValueLeesThanEqualSearchForm(model);
        assertEquals("contract/value-le-search", result);
    }

    @Test
    public void testShowStatusSearchForm() {
        String result = contractController.showStatusSearchForm(model);
        assertEquals("contract/status-search", result);
    }

    @Test
    public void testContractControllerConstructor() {
        assertNotNull(contractController);
    }
}
