package co.com.bancolombia.r2dbc.entities.loanApplication.repositories;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.bancolombia.r2dbc.entities.loanApplication.LoanApplicationEntity;

// TODO: This file is just an example, you should delete or modify it
public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, String>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

}
