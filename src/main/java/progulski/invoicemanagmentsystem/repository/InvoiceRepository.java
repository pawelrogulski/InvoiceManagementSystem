package progulski.invoicemanagmentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import progulski.invoicemanagmentsystem.domain.Invoice;

import java.util.Set;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
//    @Query("")
//    Set<Invoice> getInvoicesFromCurrentMonth();
}
