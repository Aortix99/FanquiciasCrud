package franchisesCrud.web;

import franchisesCrud.application.dto.request.CreateBranchRequest;
import franchisesCrud.application.dto.request.CreateFranchiseRequest;
import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.response.BranchResponse;
import franchisesCrud.application.dto.response.FranchiseResponse;
import franchisesCrud.application.dto.response.TopStockProductResponse;
import franchisesCrud.application.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping
    public ResponseEntity<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        FranchiseResponse response = franchiseService.createFranchise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{franchiseId}/branches")
    public ResponseEntity<BranchResponse> addBranch(
            @PathVariable Long franchiseId,
            @Valid @RequestBody CreateBranchRequest request
    ) {
        BranchResponse response = franchiseService.addBranch(franchiseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{franchiseId}/name")
    public ResponseEntity<FranchiseResponse> updateFranchiseName(
            @PathVariable Long franchiseId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        FranchiseResponse response = franchiseService.updateFranchiseName(franchiseId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{franchiseId}/top-stock-products-by-branch")
    public ResponseEntity<List<TopStockProductResponse>> findTopStockProductsPerBranch(@PathVariable Long franchiseId) {
        List<TopStockProductResponse> response = franchiseService.findTopStockProductsPerBranch(franchiseId);
        return ResponseEntity.ok(response);
    }
}
