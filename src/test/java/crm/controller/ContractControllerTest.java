package crm.controller;

import crm.entity.Contract;
import crm.service.ContractService;
import crm.service.CustomerService;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

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

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setId(1L);
        contract.setName("Test Contract");
    }

    @Test
    void testShowAllContracts() {
        when(contractService.listAllContracts()).thenReturn(Arrays.asList(contract));

        String viewName = contractController.showAllContracts(model);

        assertEquals("contract/list", viewName);
        verify(contractService).listAllContracts();
        verify(model).addAttribute(eq("contracts"), any());
    }

    @Test
    void testShowFormAddContract() {
        String viewName = contractController.showFormAddContract(model);

        assertEquals("contract/add", viewName);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
    }
}
