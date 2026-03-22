package franchisesCrud.application.dto.response;

public record BranchResponse(
        Long id,
        Long franchiseId,
        String name
) {
}
