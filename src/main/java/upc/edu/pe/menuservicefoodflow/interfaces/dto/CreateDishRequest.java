package upc.edu.pe.menuservicefoodflow.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para crear un plato con ingredientes (nombre y cantidad)
 */
public record CreateDishRequest(
        @NotBlank(message = "El nombre del plato es obligatorio")
        String name,

        @NotNull(message = "Los ingredientes son obligatorios")
        List<IngredientRequest> ingredients,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser positivo")
        BigDecimal price,

        String description
) {
    /**
     * DTO para un ingrediente con nombre y cantidad
     */
    public record IngredientRequest(
            @NotBlank(message = "El nombre del ingrediente es obligatorio")
            String name,

            @NotNull(message = "La cantidad es obligatoria")
            @Positive(message = "La cantidad debe ser positiva")
            Double quantity,

            String unit // opcional: "gramos", "ml", "unidades", etc.
    ) {}
}

