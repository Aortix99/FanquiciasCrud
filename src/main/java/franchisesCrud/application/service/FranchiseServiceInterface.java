package franchisesCrud.application.service;

import franchisesCrud.application.dto.request.CreateBranchRequest;
import franchisesCrud.application.dto.request.CreateFranchiseRequest;
import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.response.BranchResponse;
import franchisesCrud.application.dto.response.FranchiseResponse;
import franchisesCrud.application.dto.response.TopStockProductResponse;

import java.util.List;

public interface FranchiseServiceInterface {
    FranchiseResponse createFranchise(CreateFranchiseRequest request);
    BranchResponse addBranch(Long franchiseId, CreateBranchRequest request);
    FranchiseResponse updateFranchiseName(Long franchiseId, UpdateNameRequest request);
    List<TopStockProductResponse> findTopStockProductsPerBranch(Long franchiseId);
}
