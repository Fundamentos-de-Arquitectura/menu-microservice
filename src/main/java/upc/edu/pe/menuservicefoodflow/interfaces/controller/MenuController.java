package upc.edu.pe.menuservicefoodflow.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.menuservicefoodflow.application.service.MenuService;
import upc.edu.pe.menuservicefoodflow.domain.model.Dish;
import upc.edu.pe.menuservicefoodflow.infrastructure.client.SubscriptionClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;
    private final SubscriptionClient subscriptionClient;

    public MenuController(MenuService menuService, SubscriptionClient subscriptionClient) {
        this.menuService = menuService;
        this.subscriptionClient = subscriptionClient;
    }

    @PostMapping
    public ResponseEntity<?> createDish(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        String name = (String) payload.get("name");
        List<String> ingredients = (List<String>) payload.get("ingredients");
        BigDecimal price = new BigDecimal(payload.get("price").toString());
        String description = (String) payload.getOrDefault("description", "");

        try {
            Dish dish = menuService.createDish(userId, name, ingredients, price, description);
            return ResponseEntity.ok(dish);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(menuService.getAllDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        return menuService.getDishById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDish(@PathVariable Long id) {
        menuService.deleteDish(id);
        return ResponseEntity.ok(Map.of("message", "Dish deleted successfully"));
    }

    @GetMapping("/test-subscription/{userId}")
    public ResponseEntity<?> testSubscription(@PathVariable Long userId) {
        var subscription = subscriptionClient.getUserSubscription(userId);
        if (subscription == null) {
            return ResponseEntity.badRequest().body("Could not fetch subscription.");
        }
        return ResponseEntity.ok(subscription);
    }
}
