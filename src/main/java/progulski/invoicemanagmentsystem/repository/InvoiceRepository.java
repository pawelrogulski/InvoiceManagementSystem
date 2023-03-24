package progulski.invoicemanagmentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import progulski.invoicemanagmentsystem.domain.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
