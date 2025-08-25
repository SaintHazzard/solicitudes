package co.com.bancolombia.r2dbc.implementaciones.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import co.com.bancolombia.model.client.gateways.ExternalServiceClient;
import co.com.bancolombia.model.common.ResilienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientExternalServiceAdapter implements ExternalServiceClient {

    @Value("${external.service.base-url}")
    private String baseUrl;
    
    private final WebClient.Builder webClientBuilder;
    private final ResilienceService resilienceService;
    
    @Override
    public <T, R> Mono<R> callExternalService(String servicePath, T requestData, Class<R> responseType) {
        return resilienceService.executeWithResilience(() -> 
            webClientBuilder.build()
                .post()
                .uri(baseUrl + servicePath)
                .bodyValue(requestData)
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(error -> log.error("Error calling external service: {}", error.getMessage()))
        );
    }
    
    @Override
    public <R> Mono<R> getFromExternalService(String servicePath, Class<R> responseType) {
        return resilienceService.executeWithResilience(() -> 
            webClientBuilder.build()
                .get()
                .uri(baseUrl + servicePath)
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(error -> log.error("Error calling external service: {}", error.getMessage()))
        );
    }
}
