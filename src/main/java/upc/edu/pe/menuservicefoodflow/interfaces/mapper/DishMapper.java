package upc.edu.pe.menuservicefoodflow.interfaces.mapper;

import upc.edu.pe.menuservicefoodflow.domain.model.Dish;
import upc.edu.pe.menuservicefoodflow.domain.model.Ingredient;
import upc.edu.pe.menuservicefoodflow.interfaces.dto.CreateDishRequest;
import upc.edu.pe.menuservicefoodflow.interfaces.dto.DishResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre DTOs y entidades de dominio
 */
public class DishMapper {

    public static Dish toEntity(CreateDishRequest request, Long userId) {
        List<Ingredient> ingredients = request.ingredients().stream()
                .map(ingredientRequest -> {
                    if (ingredientRequest.unit() != null && !ingredientRequest.unit().isEmpty()) {
                        return new Ingredient(ingredientRequest.name(), ingredientRequest.quantity(), ingredientRequest.unit());
                    }
                    return new Ingredient(ingredientRequest.name(), ingredientRequest.quantity());
                })
                .collect(Collectors.toList());

        return new Dish(
                request.name(),
                ingredients,
                request.price(),
                request.description() != null ? request.description() : "",
                userId
        );
    }

    public static DishResponse toResponse(Dish dish) {
        List<DishResponse.IngredientResponse> ingredients = dish.getIngredients().stream()
                .map(ingredient -> new DishResponse.IngredientResponse(
                        ingredient.getName(),
                        ingredient.getQuantity(),
                        ingredient.getUnit()
                ))
                .collect(Collectors.toList());

        return new DishResponse(
                dish.getId(),
                dish.getName(),
                ingredients,
                dish.getPrice(),
                dish.getDescription(),
                dish.getUserId()
        );
    }
}

