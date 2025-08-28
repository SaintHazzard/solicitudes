package co.com.bancolombia.r2dbc.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth-test")
@RequiredArgsConstructor
@Slf4j
public class AuthTestController {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;
    private final WebClient webClient;

    @GetMapping("/token-info")
    public Mono<ResponseEntity<String>> getTokenInfo() {
        return clientRegistrationRepository.findByRegistrationId("autenticacion-client")
            .map(registration -> {
                StringBuilder info = new StringBuilder();
                info.append("OAuth2 Client Registration Info:\n");
                info.append("  ID: ").append(registration.getRegistrationId()).append("\n");
                info.append("  Client ID: ").append(registration.getClientId()).append("\n");
                info.append("  Client Secret: ").append("*".repeat(registration.getClientSecret().length())).append("\n");
                info.append("  Token URI: ").append(registration.getProviderDetails().getTokenUri()).append("\n");
                info.append("  Auth Method: ").append(registration.getClientAuthenticationMethod().getValue()).append("\n");
                info.append("  Grant Type: ").append(registration.getAuthorizationGrantType().getValue()).append("\n");
                
                return ResponseEntity.ok(info.toString());
            })
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/test-token-request")
    public Mono<ResponseEntity<String>> testTokenRequest() {
        log.info("Testing direct token request");
        
        return webClient
            .get()
            .uri("http://localhost:8090/api/v1/users/test")
            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction
                .clientRegistrationId("autenticacion-client"))
            .retrieve()
            .bodyToMono(String.class)
            .map(response -> ResponseEntity.ok("Token request successful: " + response))
            .onErrorResume(error -> {
                log.error("Error en test de token: {}", error.getMessage(), error);
                return Mono.just(ResponseEntity.status(500)
                    .body("Error en test de token: " + error.getMessage()));
            });
    }
    
    @GetMapping("/manual-token-request")
    public Mono<ResponseEntity<String>> manualTokenRequest() {
        log.info("Testing manual token request");
        
        // Usando una solicitud POST con parámetros en la URL como se hace en Postman
        String tokenUri = "http://localhost:8090/issuer/token?grant_type=client_credentials&client_id=gateway-client&client_secret=gateway-client-secret";
        
        return WebClient.builder()
            .build()
            .post()
            .uri(tokenUri)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                log.info("Token response: {}", response);
                return ResponseEntity.ok("Token obtenido correctamente: " + response);
            })
            .onErrorResume(error -> {
                log.error("Error obteniendo token manualmente: {}", error.getMessage(), error);
                return Mono.just(ResponseEntity.status(500)
                    .body("Error obteniendo token: " + error.getMessage()));
            });
    }
}
