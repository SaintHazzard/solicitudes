package co.com.bancolombia.api.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Filtro para capturar tokens de autorización de las solicitudes entrantes
 * y ponerlos en el contexto de Reactor para propagación automática a los WebClient
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TokenPropagationFilter implements WebFilter {

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        // Extrae el token del header de autorización
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader != null && !authHeader.isEmpty()) {
            log.debug("Capturando token de autenticación para propagación automática");
            
            // Ponemos el token en el contexto de Reactor para que esté disponible en toda la cadena reactiva
            return chain.filter(exchange)
                    .contextWrite(ctx -> ctx.put("authorizationToken", authHeader));
        }
        
        return chain.filter(exchange);
    }
}
