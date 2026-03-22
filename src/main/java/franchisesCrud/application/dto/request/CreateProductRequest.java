package franchisesCrud.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank(message = "Nombre del producto es requerido")
        @Size(max = 120, message = "Maximo 120 caracteres")
        String name,
        @NotNull(message = "Cantidad es requerido")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock
) {
}
