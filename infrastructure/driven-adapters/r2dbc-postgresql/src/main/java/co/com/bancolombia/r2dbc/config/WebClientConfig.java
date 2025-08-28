package co.com.bancolombia.r2dbc.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient webClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        
        // Configuración de timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .wiretap("reactor.netty.http.client.HttpClient", 
                         LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5))
                        .addHandlerLast(new WriteTimeoutHandler(5)));

        // Logging de solicitudes
        ExchangeFilterFunction loggingFilter = ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            
            // Log para los requests de token OAuth2
            if (clientRequest.url().toString().contains("/issuer/token")) {
                log.info("OAuth2 Token Request: {} {}", clientRequest.method(), clientRequest.url());
                log.info("OAuth2 Token Headers:");
                clientRequest.headers().forEach((name, values) -> 
                    values.forEach(value -> log.info("  {} = {}", name, value)));
            }
            
            return Mono.just(clientRequest);
        });
        
        // Logging de respuestas específico para OAuth2
        ExchangeFilterFunction responseLoggingFilter = ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.request().getURI().toString().contains("/issuer/token")) {
                log.info("OAuth2 Token Response Status: {}", clientResponse.statusCode());
                
                // Si es un error, intentar loggear el cuerpo de la respuesta
                if (clientResponse.statusCode().isError()) {
                    return clientResponse.bodyToMono(String.class)
                        .doOnNext(body -> log.error("OAuth2 Token Response Error Body: {}", body))
                        .map(body -> clientResponse);
                }
            }
            return Mono.just(clientResponse);
        });

        // Filtro OAuth2 usando nuestro authorizedClientManager personalizado
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = 
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(loggingFilter)
                .filter(responseLoggingFilter)
                .filter(oauth)
                .build();
    }
}
