package franchisesCrud.application.dto.interfac;

public interface BranchTopStockProjection {
    Long getBranchId();

    String getBranchName();

    Long getProductId();

    String getProductName();

    Integer getStock();
}
