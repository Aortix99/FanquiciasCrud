package franchisesCrud.application.dto.response;

public record ProductResponse(
        Long id,
        Long branchId,
        String name,
        Integer stock
) {
}
