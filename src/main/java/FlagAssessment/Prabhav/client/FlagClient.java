package FlagAssessment.Prabhav.client;

import FlagAssessment.Prabhav.DTO.EvaluationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FlagClient {

    private final RestClient restClient = RestClient.create("http://localhost:8080");

    public boolean isFeatureEnabled(String tenantId, String flag, String user) {

        EvaluationResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/flags/eval")
                        .queryParam("flag", flag)
                        .queryParam("user", user)
                        .build())
                .header("X-Tenant-ID", tenantId)
                .retrieve()
                .body(EvaluationResponse.class);

        return response != null && response.isEnabled();
    }

}