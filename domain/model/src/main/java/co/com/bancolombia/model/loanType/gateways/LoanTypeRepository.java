package co.com.bancolombia.model.loanType.gateways;


import co.com.bancolombia.model.loanType.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
  Mono<LoanType> findById(String id);
  Flux<LoanType> findAll();
  Mono<LoanType> save(LoanType loanType); 
}
