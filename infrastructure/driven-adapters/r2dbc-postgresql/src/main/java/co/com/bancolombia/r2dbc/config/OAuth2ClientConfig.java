package co.com.bancolombia.r2dbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OAuth2ClientConfig {

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        log.info("Creando registro de cliente OAuth2 para autenticacion-client");
        
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("autenticacion-client")
                .clientId("gateway-client")
                .clientSecret("gateway-client-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("read", "write")
                .tokenUri("http://localhost:8090/issuer/token")
                .build();
                
        log.info("Cliente OAuth2 configurado:");
        log.info("  ID: {}", registration.getRegistrationId());
        log.info("  Client ID: {}", registration.getClientId());
        log.info("  Auth Method: {}", registration.getClientAuthenticationMethod().getValue());
        log.info("  Token URI: {}", registration.getProviderDetails().getTokenUri());
        
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }
    
    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }
    
    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
            
        // Crear un cliente personalizado para las solicitudes de token
        ReactiveOAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest> tokenResponseClient = 
                createCustomTokenResponseClient();
        
        // Configurar el proveedor de autorización de cliente
        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = 
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials(builder -> 
                            builder.accessTokenResponseClient(tokenResponseClient))
                        .build();
        
        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = 
                new DefaultReactiveOAuth2AuthorizedClientManager(
                    clientRegistrationRepository, authorizedClientRepository);
                    
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        
        return authorizedClientManager;
    }
    
    private ReactiveOAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest> createCustomTokenResponseClient() {
        return request -> {
            log.info("Solicitando token para: {}", request.getClientRegistration().getRegistrationId());
            
            // Construir la URL con los parámetros en el query string
            String tokenUri = request.getClientRegistration().getProviderDetails().getTokenUri();
            String queryParams = String.format("?grant_type=client_credentials&client_id=%s&client_secret=%s",
                                    request.getClientRegistration().getClientId(),
                                    request.getClientRegistration().getClientSecret());
            
            String fullUri = tokenUri + queryParams;
            log.info("URL completa para solicitud de token: {}", fullUri);
            
            // Enviar la solicitud POST al endpoint de token
            return WebClient.builder().build()
                    .post()
                    .uri(fullUri)
                    .retrieve()
                    .bodyToMono(CustomTokenResponse.class)
                    .map(customResponse -> {
                        log.info("Respuesta de token recibida: {}", customResponse);
                        
                        // Convertir la respuesta al formato esperado por Spring OAuth2
                        return OAuth2AccessTokenResponse.withToken(customResponse.access_token)
                                .tokenType(org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER)
                                .expiresIn(customResponse.expires_in)
                                .scopes(parseScopes(customResponse.scope))
                                .build();
                    });
        };
    }
    
    private static class CustomTokenResponse {
        public String error;
        public String scope;
        public String token_type;
        public long expires_in;
        public String access_token;
        public String refresh_token;
        
        @Override
        public String toString() {
            return "CustomTokenResponse{" +
                    "error='" + error + '\'' +
                    ", scope='" + scope + '\'' +
                    ", token_type='" + token_type + '\'' +
                    ", expires_in=" + expires_in +
                    ", access_token='" + (access_token != null ? access_token.substring(0, 10) + "..." : null) + '\'' +
                    ", refresh_token='" + refresh_token + '\'' +
                    '}';
        }
    }
    
    private java.util.Set<String> parseScopes(String scope) {
        if (scope == null || scope.isEmpty()) {
            return java.util.Collections.emptySet();
        }
        return java.util.Arrays.stream(scope.split(" "))
                .collect(java.util.stream.Collectors.toSet());
    }
}
