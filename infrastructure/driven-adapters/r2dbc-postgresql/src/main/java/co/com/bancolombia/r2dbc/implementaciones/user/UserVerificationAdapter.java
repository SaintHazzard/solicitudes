package co.com.bancolombia.r2dbc.implementaciones.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import co.com.bancolombia.model.common.ResilienceService;
import co.com.bancolombia.model.user.User;
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

  private final WebClient webClient;
  private final ResilienceService resilienceService;

  @Override
  public Mono<User> verifyUserExists(String email) {
    log.info("Verificando si existe usuario con email: {}", email);

    // URL correcta según el router function que mostraste
    String uri = userApiUrl + "/api/v1/users?email=" + email;
    log.info("URL completa de verificación: {}", uri);

    return resilienceService.executeWithResilience(() -> webClient
        .get()
        .uri(uri)
        .retrieve()
        .bodyToMono(User.class)
        .flatMap(response -> {
          return Mono.just(response);
        })
        .onErrorResume(WebClientResponseException.class, e -> {
          log.error("Error HTTP: {} al verificar usuario: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
          return Mono.just(new User());
        })
        .onErrorResume(e -> {
          log.error("Error verificando usuario: {}", e.getMessage());
          return Mono.just(new User());
        }));
  }

  // DTO para la respuesta
  record UserVerificationResponse(String email) {
  }
}
