package co.com.bancolombia.r2dbc.implementaciones.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import co.com.bancolombia.model.common.ResilienceService;
import co.com.bancolombia.model.user.gateways.UserVerificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserVerificationAdapter implements UserVerificationPort {

  @Value("${external.service.user-api-url:http://localhost:8090}")
  private String userApiUrl;

  private final WebClient.Builder webClientBuilder;
  private final ResilienceService resilienceService;

  @Override
  public Mono<Boolean> verifyUserExists(String email) {
    log.info("Verificando si existe usuario con email: {}", email);
    return resilienceService.executeWithResilience(() -> webClientBuilder.build()
        .get()
        .uri(userApiUrl + "/api/v1/users/email/{email}", email)
        .retrieve()
        .bodyToMono(UserVerificationResponse.class)
        .flatMap(response -> {
          boolean exists = response.email() != null && !response.email().isEmpty();
          log.info("Respuesta de verificación de usuario: {}, existe: {}", response, exists);
          return Mono.just(exists);
        })
        .doOnError(error -> log.error("Error verificando usuario: {}", error.getMessage())));
  }

  @Override
  public Mono<Integer> getUserCreditScore(String email) {
    log.info("Obteniendo score crediticio del usuario: {}", email);
    return resilienceService.executeWithResilience(() -> webClientBuilder.build()
        .get()
        .uri(userApiUrl + "/api/v1/users/email", email)
        .retrieve()
        .bodyToMono(CreditScoreResponse.class)
        .map(CreditScoreResponse::score)
        .doOnError(error -> log.error("Error obteniendo credit score: {}", error.getMessage())));
  }

  // DTOs para las respuestas
  record UserVerificationResponse(String email) {
  }

  record CreditScoreResponse(int score, String message) {
  }
}
