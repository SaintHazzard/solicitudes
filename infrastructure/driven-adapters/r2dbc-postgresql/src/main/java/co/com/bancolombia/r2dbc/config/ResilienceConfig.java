package co.com.bancolombia.r2dbc.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

@Configuration
public class ResilienceConfig {

  @Bean
  @Primary // Este será el bean inyectado por defecto
  @Qualifier("defaultRetryRegistry")
  public RetryRegistry retryRegistry() {
    // Configura la política de reintentos por defecto
    RetryConfig config = RetryConfig.custom()
        .maxAttempts(3) // Número máximo de intentos (incluyendo el primer intento)
        .waitDuration(Duration.ofMillis(1000)) // Tiempo de espera entre reintentos
        .retryExceptions(Exception.class) // Excepciones que deben provocar un reintento
        .ignoreExceptions(IllegalArgumentException.class) // Excepciones que no deben provocar reintento
        .build();
    
    // Crea un registro con la configuración personalizada
    return RetryRegistry.of(config);
  }
  
  @Bean
  @Qualifier("longRetryRegistry")
  public RetryRegistry longRetryRegistry() {
    RetryConfig config = RetryConfig.custom()
        .maxAttempts(5)
        .waitDuration(Duration.ofSeconds(2))
        .build();
    
    return RetryRegistry.of(config);
  }
}
