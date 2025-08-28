package co.com.bancolombia.r2dbc.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // Configuración de timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5))
                        .addHandlerLast(new WriteTimeoutHandler(5)));

        ExchangeFilterFunction authorizationFilter = (request, next) -> Mono.deferContextual(ctx -> {
            if (ctx.hasKey("authorizationToken")) {
                String token = ctx.get("authorizationToken");
                log.debug("Propagando token desde gateway");
                
                ClientRequest newRequest = ClientRequest.from(request)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .build();
                return next.exchange(newRequest);
            }
            return next.exchange(request);
        });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(authorizationFilter)
                .build();
    }
}

