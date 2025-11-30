package upc.edu.pe.menuservicefoodflow.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

/**
 * Cliente para comunicarse con el microservicio Subscription usando RestClient
 */
@Component
public class SubscriptionClient {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionClient.class);

    private final RestClient restClient;

    public SubscriptionClient(
            @org.springframework.beans.factory.annotation.Value("${subscription.service.url}") String subscriptionServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(subscriptionServiceUrl)
                .build();
        logger.info("SubscriptionClient initialized with URL: {}", subscriptionServiceUrl);
    }

    /**
     * Obtiene la suscripción activa de un usuario
     * 
     * @param userId ID del usuario
     * @return Optional con los datos de la suscripción o vacío si no tiene
     */
    public Optional<Map<String, Object>> getUserSubscription(Long userId) {
        try {
            logger.info("Checking subscription for user {} in Subscription service", userId);

            Map<String, Object> subscription = restClient.get()
                    .uri("/api/v1/subscriptions/{userId}", userId)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
                        if (response.getStatusCode().value() == 404) {
                            // No es un error, simplemente no tiene suscripción
                            return;
                        }
                        logger.warn("HTTP error checking subscription for user {}: Status {}", userId,
                                response.getStatusCode());
                        throw new RuntimeException("Subscription check failed: " + response.getStatusCode());
                    })
                    .body(Map.class);

            if (subscription != null) {
                logger.info("User {} has active subscription: {}", userId, subscription);
            }
            return Optional.ofNullable(subscription);

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            logger.info("User {} does not have an active subscription (404)", userId);
            return Optional.empty();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            logger.warn("HTTP error checking subscription for user {}: Status {}", userId, e.getStatusCode());
            return Optional.empty();
        } catch (RuntimeException e) {
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error checking subscription for user {}: {}", userId, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
