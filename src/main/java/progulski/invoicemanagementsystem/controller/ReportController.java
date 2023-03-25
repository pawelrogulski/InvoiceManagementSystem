package progulski.invoicemanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import progulski.invoicemanagementsystem.domain.Invoice;
import progulski.invoicemanagementsystem.service.AppService;

import java.util.List;
import java.util.Set;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
public class ReportController {
    private final AppService appService;

    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/currentMonth")
    public ResponseEntity<Set<Invoice>> getInvoicesFromCurrentMonth(){
        return ResponseEntity.ok(appService.getInvoicesFromCurrentMonth());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/groupByNipOderByOccurrences")
    public ResponseEntity<List<Object>> groupByNipOderByOccurrences(){
        return ResponseEntity.ok(appService.groupByNipOderByOccurrences());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/groupByNipOderByTotalPrice")
    public ResponseEntity<List<Object>> groupByNipOderByTotalPrice(){
        return ResponseEntity.ok(appService.groupByNipOderByTotalPrice());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/sortByDate")
    public ResponseEntity<List<Invoice>> sortByDate(){
        return ResponseEntity.ok(appService.sortByDate());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/sortByNIP")
    public ResponseEntity<List<Invoice>> sortByNIP(){
        return ResponseEntity.ok(appService.sortByNIP());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/sortByTotalPrice")
    public ResponseEntity<List<Invoice>> sortByTotalPrice(){
        return ResponseEntity.ok(appService.sortByTotalPrice());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/countByCity")
    public ResponseEntity<List<Object>> countByCity(){
        return ResponseEntity.ok(appService.countByCity());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/sumByCity")
    public ResponseEntity<List<Object>> sumByCity(){
        return ResponseEntity.ok(appService.sumByCity());
    }
    @Secured({"ROLE_EMPLOYEE"})
    @GetMapping("/invoices/groupByItems")
    public ResponseEntity<List<Object>> groupByItems(){
        return ResponseEntity.ok(appService.groupByItems());
    }
}
