package franchisesCrud.application.service;

import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.request.UpdateStockRequest;
import franchisesCrud.application.dto.response.ProductResponse;
import franchisesCrud.application.exception.NotFoundException;
import franchisesCrud.domain.model.Product;
import franchisesCrud.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse updateProductStock(Long productId, UpdateStockRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado " + productId));

        product.setStock(request.stock());
        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getBranch().getId(), saved.getName(), saved.getStock());
    }

    @Transactional
    public ProductResponse updateProductName(Long productId, UpdateNameRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado " + productId));

        product.setName(request.name().trim());
        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getBranch().getId(), saved.getName(), saved.getStock());
    }
}
