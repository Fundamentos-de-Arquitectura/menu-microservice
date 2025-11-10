package upc.edu.pe.menuservicefoodflow.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Component
public class SubscriptionClient {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionClient.class);
    private static final String SUBSCRIPTION_SERVICE = "http://subscription-service";

    private final RestClient restClient;

    public SubscriptionClient(org.springframework.web.client.RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(SUBSCRIPTION_SERVICE)
                .build();
    }

    /**
     * Obtiene la suscripción activa de un usuario
     * @param userId ID del usuario
     * @return Optional con la suscripción o vacío si no existe o no está activa
     */
    public Optional<Map<String, Object>> getUserSubscription(Long userId) {
        try {
            logger.info("Fetching subscription for user: {}", userId);

            Map<String, Object> subscription = restClient.get()
                    .uri("/api/v1/subscriptions/{userId}", userId)
                    .retrieve()
                    .body(Map.class);

            if (subscription != null && "ACTIVE".equals(subscription.get("status"))) {
                return Optional.of(subscription);
            }

            logger.warn("User {} does not have an active subscription", userId);
            return Optional.empty();

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            logger.warn("Subscription not found for user {}", userId);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error fetching subscription for user {}: {}", userId, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
