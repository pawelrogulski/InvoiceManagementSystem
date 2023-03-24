package progulski.invoicemanagmentsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import progulski.invoicemanagmentsystem.domain.Invoice;
import progulski.invoicemanagmentsystem.domain.User;
import progulski.invoicemanagmentsystem.service.AppService;

import java.rmi.NoSuchObjectException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
public class AppController {
    private final AppService appService;
    @PostMapping("/register")
    public User processRegisterCustomer(@RequestBody User user) {
        return appService.addCustomer(user);
    }
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
