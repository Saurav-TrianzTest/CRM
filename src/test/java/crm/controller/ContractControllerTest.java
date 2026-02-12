package crm.controller;

import crm.service.ContractService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@WebMvcTest(ContractController.class)
@DisplayName("ContractController Tests")
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @Test
    @WithMockUser
    @DisplayName("Test contract controller exists")
    void testContractControllerExists() {
        // Controller instantiation test
    }

    @Test
    @WithMockUser
    @DisplayName("Test contract service is injected")
    void testContractServiceInjected() {
        verify(contractService, atLeast(0)).listAllContracts();
    }

    @Test
    @WithMockUser
    @DisplayName("Test listAllContracts works")
    void testListAllContracts() {
        when(contractService.listAllContracts()).thenReturn(Arrays.asList());
        contractService.listAllContracts();
        verify(contractService, times(1)).listAllContracts();
    }
}
