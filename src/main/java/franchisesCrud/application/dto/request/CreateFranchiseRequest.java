package franchisesCrud.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFranchiseRequest(
        @NotBlank(message = "Nombre de la franquicia es requerido")
        @Size(max = 120, message = "Maximo 120 caracteres")
        String name,
        String document
) {
}
