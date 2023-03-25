package progulski.invoicemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import progulski.invoicemanagementsystem.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}