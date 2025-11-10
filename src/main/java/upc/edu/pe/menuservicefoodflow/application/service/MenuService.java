package upc.edu.pe.menuservicefoodflow.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.edu.pe.menuservicefoodflow.domain.model.Dish;
import upc.edu.pe.menuservicefoodflow.domain.model.Ingredient;
import upc.edu.pe.menuservicefoodflow.domain.repository.DishRepository;
import upc.edu.pe.menuservicefoodflow.infrastructure.client.IamClient;
import upc.edu.pe.menuservicefoodflow.infrastructure.client.SubscriptionClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final DishRepository dishRepository;
    private final SubscriptionClient subscriptionClient;
    private final IamClient iamClient;

    public MenuService(DishRepository dishRepository, 
                      SubscriptionClient subscriptionClient,
                      IamClient iamClient) {
        this.dishRepository = dishRepository;
        this.subscriptionClient = subscriptionClient;
        this.iamClient = iamClient;
    }

    @Transactional
    public Dish createDish(Long userId, String name, List<Ingredient> ingredients, BigDecimal price, String description) {
        logger.info("Attempting to create dish '{}' for user {}", name, userId);

        // Validar que el usuario existe en IAM
        var userOpt = iamClient.validateUser(userId);
        if (userOpt.isEmpty()) {
            logger.error("User {} does not exist in IAM service. Please create an account first.", userId);
            throw new IllegalStateException(
                String.format("User with ID %d does not exist in the system. Please create an account in IAM service first.", userId)
            );
        }

        logger.info("User {} validated in IAM service", userId);

        // Validar que el usuario tiene una suscripción activa
        var subscriptionOpt = subscriptionClient.getUserSubscription(userId);
        if (subscriptionOpt.isEmpty()) {
            logger.warn("User {} does not have an active subscription", userId);
            throw new IllegalStateException(
                String.format("User with ID %d must have an active subscription to create a dish. Please check your subscription status.", userId)
            );
        }

        logger.info("User {} has active subscription. Creating dish '{}'", userId, name);

        Dish dish = new Dish(name, ingredients, price, description, userId);
        Dish savedDish = dishRepository.save(dish);
        logger.info("Dish '{}' created successfully with ID {}", name, savedDish.getId());
        return savedDish;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    public List<Dish> getDishesByUserId(Long userId) {
        return dishRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
