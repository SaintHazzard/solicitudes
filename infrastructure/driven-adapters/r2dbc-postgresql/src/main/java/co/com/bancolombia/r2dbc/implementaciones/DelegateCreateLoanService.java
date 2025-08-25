package co.com.bancolombia.r2dbc.implementaciones;

import java.util.List;

import org.springframework.stereotype.Service;

import co.com.bancolombia.model.common.CrearStrategyEnum;
import co.com.bancolombia.model.common.ReactiveTx;
import co.com.bancolombia.model.common.ResilienceService;
import co.com.bancolombia.model.loanApplication.LoanApplication;
import co.com.bancolombia.model.loanApplication.gateways.CreateLoanPort;
import co.com.bancolombia.model.loanApplication.gateways.StrategyLoanFactory;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Service
public class DelegateCreateLoanService implements CreateLoanPort {


  private final ReactiveTx reactiveTx;
  private final ResilienceService resilienceService;
  private final StrategyLoanFactory factory;

  @Override
  public Mono<LoanApplication> create(LoanApplication loanApplication) {
    return factory.getStrategy(CrearStrategyEnum.SIMPLE).create(loanApplication);
  }

  @Override
  public Mono<LoanApplication> processLoanApplication(LoanApplication loanApplication) {
    return factory.getStrategy(CrearStrategyEnum.SIMPLE).create(loanApplication);
  }

  @Override
  public Mono<LoanApplication> processLoanApplicationWithResilience(LoanApplication loanApplication) {
    return resilienceService.executeWithResilience(
        () -> reactiveTx.write(() -> factory.getStrategy(CrearStrategyEnum.RESILIENCE).create(loanApplication))
    );
  }

  @Override
  public Mono<LoanApplication> processLoanApplicationWithStrategy(LoanApplication loanApplication,
      CrearStrategyEnum strategy) {
    return factory.getStrategy(strategy).create(loanApplication);
  }

  @Override
  public Mono<Void> createMultipleLoan(List<LoanApplication> loanApplications) {
    return reactiveTx.write(() -> 
        Mono.when(loanApplications.stream()
            .map(this::create)
            .toList())
            .then()
    );
  }

  @Override
  public Mono<Void> createTwoLoans(LoanApplication loanApplication1, LoanApplication loanApplication2) {
    return reactiveTx.write(() -> 
        Mono.when(
            create(loanApplication1),
            create(loanApplication2)
        ).then()
    );
  }
  
}
