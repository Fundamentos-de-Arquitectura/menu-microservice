package upc.edu.pe.menuservicefoodflow.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

/**
 * Cliente para comunicarse con el microservicio IAM usando RestClient
 */
@Component
public class IamClient {

    private static final Logger logger = LoggerFactory.getLogger(IamClient.class);

    private final RestClient restClient;

    public IamClient(@org.springframework.beans.factory.annotation.Value("${iam.service.url}") String iamServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(iamServiceUrl)
                .build();
        logger.info("IamClient initialized with URL: {}", iamServiceUrl);
    }

    /**
     * Valida que un usuario exista en el sistema IAM
     * 
     * @param userId ID del usuario
     * @return Optional con los datos del usuario o vacío si no existe
     */
    public Optional<Map<String, Object>> validateUser(Long userId) {
        try {
            logger.info("Validating user in IAM service: {}", userId);

            Map<String, Object> user = restClient.get()
                    .uri("/api/v1/accounts/{userId}/user-response", userId)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
                        logger.warn("HTTP error validating user {} in IAM service: Status {}", userId,
                                response.getStatusCode());
                        throw new RuntimeException("User validation failed: " + response.getStatusCode());
                    })
                    .body(Map.class);

            if (user != null) {
                logger.info("User {} validated successfully in IAM service", userId);
            }
            return Optional.ofNullable(user);

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            logger.warn("User {} not found in IAM service (404)", userId);
            return Optional.empty();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            logger.warn("HTTP error validating user {} in IAM service: Status {}", userId, e.getStatusCode());
            return Optional.empty();
        } catch (RuntimeException e) {
            // Ya logueado arriba
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error validating user {} in IAM service: {}", userId, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
