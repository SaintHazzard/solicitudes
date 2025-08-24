package co.com.bancolombia.api.helper;



import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class ValidationHandler {

  private final Validator validator;

  public ValidationHandler(Validator validator) {
    this.validator = validator;
  }

  /** Valida un objeto reactivamente (sin grupos). */
  public <T> Mono<T> validate(T object) {
    return Mono.defer(() -> {
      Set<ConstraintViolation<T>> violations = validator.validate(object);
      if (!violations.isEmpty())
        return Mono.error(new ConstraintViolationException(violations));
      return Mono.just(object);
    });
  }

  /** Valida con grupos. */
  public <T> Mono<T> validate(T object, Class<?>... groups) {
    return Mono.defer(() -> {
      Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
      if (!violations.isEmpty())
        return Mono.error(new ConstraintViolationException(violations));
      return Mono.just(object);
    });
  }

  /** Azúcar: valida un Mono ya existente. */
  public <T> Mono<T> validateMono(Mono<T> mono, Class<?>... groups) {
    return mono.flatMap(obj -> validate(obj, groups));
  }
}
