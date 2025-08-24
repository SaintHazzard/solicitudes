package co.com.bancolombia.r2dbc.entities.loanType.repositories;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.bancolombia.r2dbc.entities.loanType.LoanTypeEntity;

// TODO: This file is just an example, you should delete or modify it
public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, String>, ReactiveQueryByExampleExecutor<LoanTypeEntity> {

}
