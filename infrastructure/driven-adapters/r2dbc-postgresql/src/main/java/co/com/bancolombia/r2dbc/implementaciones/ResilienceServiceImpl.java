package co.com.bancolombia.r2dbc.implementaciones;


import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.com.bancolombia.model.common.ResilienceService;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.reactor.retry.RetryOperator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;




@Service
@Slf4j
public class ResilienceServiceImpl implements ResilienceService {

  private final Retry defaultRetry;
  private final Retry longRetry;

  public ResilienceServiceImpl(
      @Qualifier("defaultRetryRegistry") RetryRegistry defaultRetryRegistry,
      @Qualifier("longRetryRegistry") RetryRegistry longRetryRegistry) {
    this.defaultRetry = defaultRetryRegistry.retry("default");
    this.longRetry = longRetryRegistry.retry("long");
  }

  @Override
  public <T> Mono<T> executeWithResilience(Supplier<Mono<T>> operation) {
    return Mono.defer(operation)
        .transformDeferred(RetryOperator.of(defaultRetry));
  }
  
  /**
   * Ejecuta una operación con una política de reintentos más larga.
   * Útil para operaciones críticas que requieren más intentos.
   */
  public <T> Mono<T> executeWithLongResilience(Supplier<Mono<T>> operation) {
    return Mono.defer(operation)
        .transformDeferred(RetryOperator.of(longRetry));
  }
}
