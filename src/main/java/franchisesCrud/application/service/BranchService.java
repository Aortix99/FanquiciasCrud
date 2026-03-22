package franchisesCrud.application.service;

import franchisesCrud.application.dto.request.CreateProductRequest;
import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.response.BranchResponse;
import franchisesCrud.application.dto.response.ProductResponse;
import franchisesCrud.application.exception.NotFoundException;
import franchisesCrud.domain.model.Branch;
import franchisesCrud.domain.model.Product;
import franchisesCrud.infrastructure.repository.BranchRepository;
import franchisesCrud.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class BranchService implements BranchServiceInterface {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public BranchService(BranchRepository branchRepository, ProductRepository productRepository) {
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse addProduct(Long branchId, CreateProductRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con el id " + branchId));

        Product product = new Product();
        product.setBranch(branch);
        product.setName(request.name().trim());
        product.setStock(request.stock());
        Product saved = productRepository.save(product);

        return new ProductResponse(saved.getId(), branchId, saved.getName(), saved.getStock());
    }

    @Transactional
    public void deleteProduct(Long branchId, Long productId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con el id " + branchId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con el id " + productId));

        if (!product.getBranch().getId().equals(branch.getId())) {
            throw new NotFoundException("producto " + productId + " no pertenece a la sucursal " + branchId);
        }

        productRepository.delete(product);
    }

    @Transactional
    public BranchResponse updateBranchName(Long branchId, UpdateNameRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("sucursal no encontrada con el id " + branchId));

        branch.setName(request.name().trim());
        Branch saved = branchRepository.save(branch);
        return new BranchResponse(saved.getId(), saved.getFranchise().getId(), saved.getName());
    }
}
