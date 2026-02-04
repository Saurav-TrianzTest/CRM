package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import crm.utils.WriteCsvToResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class CSVController {

    private CustomerService customerService;

    public CSVController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/customers", produces = "text/csv")
    public void findCustomers(HttpServletResponse httpServletResponse) throws IOException {
        List<Customer> customers = (List<Customer>) customerService.listAllCustomers();
        WriteCsvToResponse.writeCustomers(httpServletResponse.getWriter(), customers);
    }

    @GetMapping(value = "/customers/{id}", produces = "text/csv")
    public void findCustomer(@PathVariable Long id, HttpServletResponse httpServletResponse) throws IOException {
        Customer customer = customerService.showCustomer(id);
        WriteCsvToResponse.writeCustomer(httpServletResponse.getWriter(), customer);
    }

//    @GetMapping("/show-import")
//    public String showImportCsvSite() {
//        return "csv/import";
//    }

    /*
     * CSV import functionality removed - incompatible with cloud environments
     * For cloud deployment, use:
     * 1. REST API endpoint to accept CSV file uploads
     * 2. Store uploaded files in S3 bucket
     * 3. Process CSV asynchronously using message queue (SQS)
     * 4. Implement pagination and streaming for large files
     */

//    @GetMapping("/show")
//    public String showPageWithCsvImported(@ModelAttribute List<String[]> data) {
//        data.
//        return "csv/show";
//    }

}
