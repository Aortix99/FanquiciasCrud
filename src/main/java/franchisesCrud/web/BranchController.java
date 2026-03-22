package franchisesCrud.web;

import franchisesCrud.application.dto.request.CreateProductRequest;
import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.response.BranchResponse;
import franchisesCrud.application.dto.response.ProductResponse;
import franchisesCrud.application.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> addProduct(
            @PathVariable Long branchId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        ProductResponse response = branchService.addProduct(branchId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{branchId}/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long branchId, @PathVariable Long productId) {
        branchService.deleteProduct(branchId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{branchId}/name")
    public ResponseEntity<BranchResponse> updateBranchName(
            @PathVariable Long branchId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        BranchResponse response = branchService.updateBranchName(branchId, request);
        return ResponseEntity.ok(response);
    }
}
