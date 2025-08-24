package co.com.bancolombia.usecase.loan.createLoan;

import co.com.bancolombia.model.common.CrearStrategy;
import co.com.bancolombia.model.common.CrearStrategyEnum;
import co.com.bancolombia.model.loanApplication.LoanApplication;
import co.com.bancolombia.model.loanApplication.gateways.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateLoanResilienceUseCase implements CrearStrategy<LoanApplication> {

  private final LoanApplicationRepository loanApplicationRepository;

  @Override
  public CrearStrategyEnum getType() {
    return CrearStrategyEnum.RESILIENCE;
  }

  @Override
  public Mono<LoanApplication> create(LoanApplication user) {
    return loanApplicationRepository.save(user);
  }

}
