package franchisesCrud.infrastructure.repository;

import franchisesCrud.domain.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
