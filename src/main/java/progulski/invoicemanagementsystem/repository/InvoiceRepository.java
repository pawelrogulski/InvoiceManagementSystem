package progulski.invoicemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import progulski.invoicemanagementsystem.domain.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query("SELECT i FROM Invoice i WHERE i.date >= :startDate")
    Set<Invoice> getInvoicesAfterDate(@Param("startDate")LocalDate firstDayOfMonth);
    @Query("SELECT i.NIP, COUNT(i.NIP) FROM Invoice i GROUP BY i.NIP ORDER BY COUNT(i.NIP) DESC")
    List<Object> groupByNipOderByOccurrences();
    @Query("SELECT i.NIP, MAX(i.totalPrice) FROM Invoice i GROUP BY i.NIP ORDER BY MAX(i.totalPrice) DESC")
    List<Object> groupByNipOderByTotalPrice();
    @Query("SELECT i FROM Invoice i ORDER BY i.date DESC")
    List<Invoice> sortByDate();
    @Query("SELECT i FROM Invoice i ORDER BY i.NIP")
    List<Invoice> sortByNIP();
    @Query("SELECT i FROM Invoice i ORDER BY i.totalPrice DESC")
    List<Invoice> sortByTotalPrice();
    @Query("SELECT i.city, COUNT(i.city) FROM Invoice i GROUP BY i.city ORDER BY COUNT(i.city) DESC")
    List<Object> countByCity();
    @Query("SELECT i.city, SUM(i.totalPrice) FROM Invoice i GROUP BY i.totalPrice ORDER BY SUM(i.totalPrice) DESC")
    List<Object> sumByCity();
    @Query("SELECT item,invoice FROM Item item JOIN Invoice invoice ON item.invoice.id=invoice.id")
    List<Object> groupByItems();
}
