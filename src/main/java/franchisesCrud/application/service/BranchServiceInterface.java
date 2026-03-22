package franchisesCrud.application.service;

import franchisesCrud.application.dto.request.CreateProductRequest;
import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.response.BranchResponse;
import franchisesCrud.application.dto.response.ProductResponse;

public interface BranchServiceInterface {
    ProductResponse addProduct(Long branchId, CreateProductRequest request);
    void deleteProduct(Long branchId, Long productId);
    BranchResponse updateBranchName(Long branchId, UpdateNameRequest request);
}
