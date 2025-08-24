package co.com.bancolombia.model.common;

import java.util.function.Supplier;

import reactor.core.publisher.Mono;

public interface ResilienceService {
   /**
    * Ejecuta una operación con una política de reintentos estándar.
    */
   public <T> Mono<T> executeWithResilience(Supplier<Mono<T>> operation);
   
   /**
    * Ejecuta una operación con una política de reintentos más larga.
    * Útil para operaciones críticas que requieren más intentos.
    */
   public <T> Mono<T> executeWithLongResilience(Supplier<Mono<T>> operation);
}
