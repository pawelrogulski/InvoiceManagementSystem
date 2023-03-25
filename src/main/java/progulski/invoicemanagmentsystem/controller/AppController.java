package progulski.invoicemanagmentsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import progulski.invoicemanagmentsystem.domain.Invoice;
import progulski.invoicemanagmentsystem.domain.Item;
import progulski.invoicemanagmentsystem.domain.User;
import progulski.invoicemanagmentsystem.service.AppService;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.util.Date;
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
    public ResponseEntity<Invoice> getInvoice(@PathVariable String id) throws NoSuchObjectException, JsonProcessingException {
        return ResponseEntity.ok(appService.getInvoice(Integer.parseInt(id)));
    }
//    public ResponseEntity<Set<Invoice>> getInvoicesFromCurrentMonth(){
//        return ResponseEntity.ok(appService.getInvoicesFromCurrentMonth());
//    }
}
