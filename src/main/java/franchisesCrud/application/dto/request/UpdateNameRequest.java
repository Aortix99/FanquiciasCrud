package franchisesCrud.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNameRequest(
        @NotBlank(message = "Nombre es requerdo")
        @Size(max = 120, message = "Maximo 120 caracteres")
        String name
) {
}
