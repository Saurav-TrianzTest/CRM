package crm.controller;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.service.ContractService;
import crm.service.CustomerService;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractControllerTest {

    private ContractController controller;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ContractController(contractService, customerService, userService);
    }

    @Test
    void testShowAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        when(contractService.listAllContracts()).thenReturn(contracts);

        String result = controller.showAllContracts(model);

        assertEquals("contract/list", result);
        verify(model).addAttribute("contracts", contracts);
    }

    @Test
    void testShowFormAddContract() {
        List<Customer> customers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        when(customerService.findAllByEnabledTrue()).thenReturn(customers);
        when(userService.listAllUsers()).thenReturn(users);

        String result = controller.showFormAddContract(model);

        assertEquals("contract/add", result);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
        verify(model).addAttribute("customers", customers);
        verify(model).addAttribute("users", users);
    }

    @Test
    void testProcessRequestAddContractSuccess() {
        Contract contract = new Contract();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = controller.processRequestAddContract(contract, bindingResult);

        assertEquals("contract/success", result);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testProcessRequestAddContractWithErrors() {
        Contract contract = new Contract();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = controller.processRequestAddContract(contract, bindingResult);

        assertEquals("redirect:/contract/add", result);
        verify(contractService, never()).saveContract(contract);
    }

    @Test
    void testShowFormEditContract() {
        Contract contract = new Contract();
        when(contractService.showContract(1L)).thenReturn(contract);

        String result = controller.showFormEditContract(model, 1L);

        assertEquals("contract/edit", result);
        verify(model).addAttribute("contract", contract);
    }

    @Test
    void testProcessRequestEditContractSuccess() {
        Contract contract = new Contract();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = controller.processRequestEditContract(1L, contract, bindingResult);

        assertEquals("redirect:/contract/list", result);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testShowNameSearchForm() {
        String result = controller.showNameSearchForm(model);

        assertEquals("contract/name-search", result);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
    }

    @Test
    void testShowStatusSearchForm() {
        String result = controller.showStatusSearchForm(model);

        assertEquals("contract/status-search", result);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
    }

    @Test
    void testShowValueLeesThanEqualSearchForm() {
        String result = controller.showValueLeesThanEqualSearchForm(model);

        assertEquals("contract/value-le-search", result);
    }

    @Test
    void testShowBeginDateSearchForm() {
        String result = controller.showBeginDateSearchForm(model);

        assertEquals("contract/begin-date-search", result);
    }
}
