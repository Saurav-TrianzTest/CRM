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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private Contract testContract;

    @BeforeEach
    public void setUp() {
        testContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .value(new BigDecimal("10000"))
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(6))
                .status(Status.PROPOSED)
                .build();
    }

    @Test
    public void testShowAllContracts() {
        when(contractService.listAllContracts()).thenReturn(new ArrayList<>());
        String view = contractController.showAllContracts(model);
        assertEquals("contract/list", view);
        verify(model, times(1)).addAttribute(eq("contracts"), any());
    }

    @Test
    public void testShowFormAddContract() {
        when(customerService.findAllByEnabledTrue()).thenReturn(new ArrayList<>());
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());
        
        String view = contractController.showFormAddContract(model);
        
        assertEquals("contract/add", view);
        verify(model, times(1)).addAttribute(eq("contract"), any(Contract.class));
        verify(model, times(1)).addAttribute(eq("customers"), any());
        verify(model, times(1)).addAttribute(eq("users"), any());
    }

    @Test
    public void testProcessRequestAddContractWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = contractController.processRequestAddContract(testContract, bindingResult);
        assertEquals("redirect:/contract/add", view);
    }

    @Test
    public void testProcessRequestAddContractSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = contractController.processRequestAddContract(testContract, bindingResult);
        assertEquals("contract/success", view);
        verify(contractService, times(1)).saveContract(testContract);
    }

    @Test
    public void testShowFormEditContract() {
        when(contractService.showContract(1L)).thenReturn(testContract);
        String view = contractController.showFormEditContract(model, 1L);
        assertEquals("contract/edit", view);
        verify(model, times(1)).addAttribute("contract", testContract);
    }

    @Test
    public void testShowNameSearchForm() {
        String view = contractController.showNameSearchForm(model);
        assertEquals("contract/name-search", view);
        verify(model, times(1)).addAttribute(eq("contract"), any(Contract.class));
    }

    @Test
    public void testProcessRequestNameSearch() {
        when(contractService.findByName(anyString())).thenReturn(testContract);
        String view = contractController.processRequestNameSearch(testContract, model);
        assertEquals("contract/show-one", view);
        verify(model, times(1)).addAttribute("contract", testContract);
    }
}
