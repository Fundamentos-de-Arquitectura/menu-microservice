package upc.edu.pe.menuservicefoodflow.interfaces.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para respuesta de un plato
 */
public record DishResponse(
        Long id,
        String name,
        List<IngredientResponse> ingredients,
        BigDecimal price,
        String description,
        Long userId
) {
    /**
     * DTO para respuesta de un ingrediente
     */
    public record IngredientResponse(
            String name,
            Double quantity,
            String unit
    ) {}
}

