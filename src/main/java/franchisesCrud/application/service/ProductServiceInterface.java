package franchisesCrud.application.service;

import franchisesCrud.application.dto.request.UpdateNameRequest;
import franchisesCrud.application.dto.request.UpdateStockRequest;
import franchisesCrud.application.dto.response.ProductResponse;

public interface ProductServiceInterface {
    ProductResponse updateProductStock(Long productId, UpdateStockRequest request);
    ProductResponse updateProductName(Long productId, UpdateNameRequest request);
}
