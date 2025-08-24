package co.com.bancolombia.model.state.gateways;


import co.com.bancolombia.model.state.State;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StateRepository {
  Mono<State> findById(String id);
  Flux<State> findAll();
  Mono<State> save(State state);
}
