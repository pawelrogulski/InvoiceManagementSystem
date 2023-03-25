package progulski.invoicemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import progulski.invoicemanagementsystem.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
        Role findByName(String name);
}
