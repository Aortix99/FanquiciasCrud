package franchisesCrud.application.dto.response;

public record TopStockProductResponse(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {
}
