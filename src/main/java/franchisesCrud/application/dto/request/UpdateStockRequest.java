package franchisesCrud.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStockRequest(
        @NotNull(message = "Stock es requedido")
        @Min(value = 0, message = "cantidades no pueden ser negativasssssss")
        Integer stock
) {
}
