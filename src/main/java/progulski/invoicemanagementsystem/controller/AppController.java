package progulski.invoicemanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import progulski.invoicemanagementsystem.domain.Invoice;
import progulski.invoicemanagementsystem.domain.User;
import progulski.invoicemanagementsystem.service.AppService;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
public class AppController {
    private final AppService appService;
    @PostMapping("/register")
    public User processRegisterCustomer(@RequestBody User user) {
        return appService.addCustomer(user);
    }
    @Secured({"ROLE_EMPLOYEE"})
    @PostMapping("/registerEmployee")
    public User processRegisterEmployee(@RequestBody User user) {
        return appService.addEmployee(user);
    }
    @Secured({"ROLE_CUSTOMER","ROLE_EMPLOYEE"})
    @PostMapping("/addInvoice")
    public Invoice addInvoice(@RequestBody Invoice invoice){
        return appService.addInvoice(invoice);
    }
    @Secured({"ROLE_CUSTOMER","ROLE_EMPLOYEE"})
    @GetMapping("/invoice/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable String id) throws NoSuchObjectException {
        return ResponseEntity.ok(appService.getInvoice(Integer.parseInt(id)));
    }
}
