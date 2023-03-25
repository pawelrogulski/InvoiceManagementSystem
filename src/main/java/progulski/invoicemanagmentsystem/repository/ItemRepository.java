package progulski.invoicemanagmentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import progulski.invoicemanagmentsystem.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}