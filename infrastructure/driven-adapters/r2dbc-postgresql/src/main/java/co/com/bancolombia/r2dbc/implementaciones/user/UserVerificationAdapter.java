package co.com.bancolombia.r2dbc.implementaciones.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

  private final WebClient webClient;
  private final ResilienceService resilienceService;

  @Override
  public Mono<Boolean> verifyUserExists(String email) {
    log.info("Verificando si existe usuario con email: {}", email);

    String uri = userApiUrl + "/api/v1/users/email?email=" + email;
    log.info("URL completa de verificación: {}", uri);

    return resilienceService.executeWithResilience(() -> webClient
        .get()
        .uri(uri)
        .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction
            .clientRegistrationId("autenticacion-client"))
        .retrieve()
        .bodyToMono(UserVerificationResponse.class)
        .doOnSubscribe(s -> log.info("Enviando solicitud de verificación con autenticación OAuth2"))
        .flatMap(response -> {
          boolean exists = response.email() != null && !response.email().isEmpty();
          log.info("Respuesta de verificación de usuario: {}, existe: {}", response, exists);
          return Mono.just(exists);
        })
        .onErrorResume(WebClientResponseException.class, e -> {
          int statusCode = e.getStatusCode().value();
          String reasonPhrase = e.getStatusCode().toString();
          log.error("Error HTTP: {} - {} al verificar usuario", statusCode, reasonPhrase);
          log.error("Cuerpo de respuesta: {}", e.getResponseBodyAsString());
          
          if (statusCode == 401 || statusCode == 403) {
            log.error("Error de autenticación OAuth2. Verificar configuración del cliente y las credenciales.");
          }
          
          return Mono.just(false); // Retornar false en caso de error para continuar el flujo
        })
        .doOnError(err -> {
          log.error("Error verificando usuario: {}", err.getMessage());

          if (err instanceof ClientAuthorizationException) {
            log.error("Error de autorización OAuth2. Detalles: {}", err.getMessage());
            log.error("Revisando configuración OAuth2:");
            log.error("  - Token URI: http://localhost:8090/issuer/token");
            log.error("  - Client ID: gateway-client");
            log.error("  - Client Secret: gateway-client-secret (longitud: {})", "gateway-client-secret".length());
            log.error("  - Grant Type: client_credentials");
            log.error("  - Client Authentication Method: CLIENT_SECRET_POST");
            log.error("Asegúrese de que el servicio en {} está activo y configurado correctamente", userApiUrl);
          }

          // Muestra la causa raíz completa para mejor diagnóstico
          Throwable rootCause = err;
          while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
          }
          log.error("Causa raíz del error: {}", rootCause.getMessage());
        }));
  }

  // DTOs para las respuestas
  static record UserVerificationResponse(String email) {
  }

  static record CreditScoreResponse(int score, String message) {
  }
}
