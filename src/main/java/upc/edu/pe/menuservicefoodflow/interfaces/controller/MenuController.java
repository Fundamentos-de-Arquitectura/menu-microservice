package upc.edu.pe.menuservicefoodflow.interfaces.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.menuservicefoodflow.application.service.MenuService;
import upc.edu.pe.menuservicefoodflow.domain.model.Dish;
import upc.edu.pe.menuservicefoodflow.interfaces.dto.CreateDishRequest;
import upc.edu.pe.menuservicefoodflow.interfaces.dto.DishResponse;
import upc.edu.pe.menuservicefoodflow.interfaces.mapper.DishMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Crea un nuevo plato en el menú del restaurante
     * Requiere que el usuario tenga una suscripción activa
     * 
     * @param userId ID del usuario que crea el plato
     * @param request DTO con la información del plato e ingredientes (nombre y cantidad)
     * @return El plato creado
     */
    @PostMapping("/users/{userId}/dishes")
    public ResponseEntity<?> createDish(
            @PathVariable Long userId,
            @Valid @RequestBody CreateDishRequest request) {
        try {
            Dish dish = DishMapper.toEntity(request, userId);
            Dish createdDish = menuService.createDish(
                    userId,
                    dish.getName(),
                    dish.getIngredients(),
                    dish.getPrice(),
                    dish.getDescription()
            );
            DishResponse response = DishMapper.toResponse(createdDish);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene todos los platos del menú para un usuario específico
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<DishResponse>> getAllDishes(@PathVariable Long userId) {
        List<DishResponse> dishes = menuService.getDishesByUserId(userId).stream()
                .map(DishMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dishes);
    }

    /**
     * Obtiene un plato por su ID verificando que pertenece al usuario
     */
    @GetMapping("/users/{userId}/{id}")
    public ResponseEntity<DishResponse> getDishById(
            @PathVariable Long userId,
            @PathVariable Long id) {
        return menuService.getDishByIdAndUserId(id, userId)
                .map(DishMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los platos creados por un usuario específico
     */
    @GetMapping("/users/{userId}/dishes")
    public ResponseEntity<List<DishResponse>> getDishesByUserId(@PathVariable Long userId) {
        List<DishResponse> dishes = menuService.getDishesByUserId(userId).stream()
                .map(DishMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dishes);
    }

    /**
     * Elimina un plato del menú verificando que pertenece al usuario
     */
    @DeleteMapping("/users/{userId}/{id}")
    public ResponseEntity<Map<String, String>> deleteDish(
            @PathVariable Long userId,
            @PathVariable Long id) {
        try {
            menuService.deleteDishByIdAndUserId(id, userId);
            return ResponseEntity.ok(Map.of("message", "Dish deleted successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
