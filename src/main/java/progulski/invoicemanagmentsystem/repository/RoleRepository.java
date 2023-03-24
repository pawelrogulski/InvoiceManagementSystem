package progulski.invoicemanagmentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import progulski.invoicemanagmentsystem.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
        Role findByName(String name);
}
