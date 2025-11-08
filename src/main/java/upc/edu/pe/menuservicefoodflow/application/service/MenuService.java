package upc.edu.pe.menuservicefoodflow.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.edu.pe.menuservicefoodflow.domain.model.Dish;
import upc.edu.pe.menuservicefoodflow.domain.repository.DishRepository;
import upc.edu.pe.menuservicefoodflow.infrastructure.client.SubscriptionClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MenuService {

    private final DishRepository dishRepository;
    private final SubscriptionClient subscriptionClient;

    public MenuService(DishRepository dishRepository, SubscriptionClient subscriptionClient) {
        this.dishRepository = dishRepository;
        this.subscriptionClient = subscriptionClient;
    }

    @Transactional
    public Dish createDish(Long userId, String name, List<String> ingredients, BigDecimal price, String description) {
        Map<String, Object> subscription = subscriptionClient.getUserSubscription(userId);

        if (subscription == null || !"ACTIVE".equals(subscription.get("status"))) {
            throw new IllegalStateException("User must have an active subscription to create a dish");
        }

        Dish dish = new Dish(name, ingredients, price, description);
        return dishRepository.save(dish);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    @Transactional
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
