package co.com.bancolombia.model.loanApplication.gateways;


import co.com.bancolombia.model.loanApplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {
  Mono<LoanApplication> findById(String id);
  Flux<LoanApplication> findAll();
  Mono<LoanApplication> save(LoanApplication loanApplication);
}
