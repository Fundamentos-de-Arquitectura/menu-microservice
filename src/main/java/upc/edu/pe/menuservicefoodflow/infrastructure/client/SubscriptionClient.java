package upc.edu.pe.menuservicefoodflow.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Component
public class SubscriptionClient {

    private final RestClient restClient;

    public SubscriptionClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String, Object> getUserSubscription(Long userId) {
        String url = "http://localhost:8083/api/v1/subscriptions/" + userId;

        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Map.class);
        } catch (RestClientResponseException e) {
            System.out.println("Error fetching subscription: " + e.getMessage());
            return null;
        }
    }
}
