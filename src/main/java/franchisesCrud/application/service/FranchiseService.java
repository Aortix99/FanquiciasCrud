package franchisesCrud.application.service;

import franchisesCrud.application.dto.request.CreateBranchRequest;
import franchisesCrud.application.dto.request.CreateFranchiseRequest;
import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.response.BranchResponse;
import franchisesCrud.application.dto.response.FranchiseResponse;
import franchisesCrud.application.dto.response.TopStockProductResponse;
import franchisesCrud.application.exception.NotFoundException;
import franchisesCrud.domain.model.Branch;
import franchisesCrud.domain.model.Franchise;
import franchisesCrud.infrastructure.repository.BranchRepository;
import franchisesCrud.infrastructure.repository.FranchiseRepository;
import franchisesCrud.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FranchiseService implements FranchiseServiceInterface {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public FranchiseService(
            FranchiseRepository franchiseRepository,
            BranchRepository branchRepository,
            ProductRepository productRepository
    ) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public FranchiseResponse createFranchise(CreateFranchiseRequest request) {
        Franchise franchise = new Franchise();
        franchise.setName(request.name().trim());
        Franchise saved = franchiseRepository.save(franchise);
        return new FranchiseResponse(saved.getId(), saved.getName());
    }

    @Transactional
    public BranchResponse addBranch(Long franchiseId, CreateBranchRequest request) {
        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new NotFoundException("Franquicia no encontrad  " + franchiseId));

        Branch branch = new Branch();
        branch.setFranchise(franchise);
        branch.setName(request.name().trim());
        Branch saved = branchRepository.save(branch);

        return new BranchResponse(saved.getId(), franchiseId, saved.getName());
    }

    @Transactional
    public FranchiseResponse updateFranchiseName(Long franchiseId, UpdateNameRequest request) {
        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new NotFoundException("Franquicia no encontrad " + franchiseId));

        franchise.setName(request.name().trim());
        Franchise saved = franchiseRepository.save(franchise);
        return new FranchiseResponse(saved.getId(), saved.getName());
    }

    @Transactional(readOnly = true)
    public List<TopStockProductResponse> findTopStockProductsPerBranch(Long franchiseId) {
        if (!franchiseRepository.existsById(franchiseId)) {
            throw new NotFoundException("Franquicia no encontrad  " + franchiseId);
        }

        return productRepository.findTopStockProductsByFranchise(franchiseId).stream()
                .map(row -> new TopStockProductResponse(
                        row.getBranchId(),
                        row.getBranchName(),
                        row.getProductId(),
                        row.getProductName(),
                        row.getStock()
                ))
                .toList();
    }
}
