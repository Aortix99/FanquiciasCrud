package franchisesCrud.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBranchRequest(
        @NotBlank(message = "Nombre de la sucursal es requerido")
        @Size(max = 120, message = "Maximo 120 caracteres")
        String name
) {
}
