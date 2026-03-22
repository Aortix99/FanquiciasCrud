package franchisesCrud.infrastructure.repository;

import franchisesCrud.application.dto.interfac.BranchTopStockProjection;
import franchisesCrud.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            SELECT
                b.id AS branchId,
                b.name AS branchName,
                p.id AS productId,
                p.name AS productName,
                p.stock AS stock
            FROM branch b
            JOIN product p ON p.branch_id = b.id
            WHERE b.franchise_id = :franchiseId
              AND p.id = (
                    SELECT p2.id
                    FROM product p2
                    WHERE p2.branch_id = b.id
                    ORDER BY p2.stock DESC, p2.id ASC
                    LIMIT 1
              )
            ORDER BY b.name
            """, nativeQuery = true)
    List<BranchTopStockProjection> findTopStockProductsByFranchise(@Param("franchiseId") Long franchiseId);
}
