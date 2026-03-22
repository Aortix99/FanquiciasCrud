package franchisesCrud.infrastructure.repository;

import franchisesCrud.domain.model.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseRepository extends JpaRepository<Franchise, Long> {
}
