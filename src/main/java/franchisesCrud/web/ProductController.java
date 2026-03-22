package franchisesCrud.web;

import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.request.UpdateStockRequest;
import franchisesCrud.application.dto.response.ProductResponse;
import franchisesCrud.application.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateStockRequest request
    ) {
        ProductResponse response = productService.updateProductStock(productId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productId}/name")
    public ResponseEntity<ProductResponse> updateProductName(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        ProductResponse response = productService.updateProductName(productId, request);
        return ResponseEntity.ok(response);
    }
}
