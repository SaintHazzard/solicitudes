package co.com.bancolombia.r2dbc.entities.state.repositories;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.bancolombia.r2dbc.entities.state.StateEntity;

// TODO: This file is just an example, you should delete or modify it
public interface StateReactiveRepository extends ReactiveCrudRepository<StateEntity, String>, ReactiveQueryByExampleExecutor<StateEntity> {

}
