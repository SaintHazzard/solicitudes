package co.com.bancolombia.model.loanApplication.gateways;

import java.util.List;

import co.com.bancolombia.model.common.CrearStrategyEnum;
import co.com.bancolombia.model.loanApplication.LoanApplication;
import reactor.core.publisher.Mono;

public interface CreateLoanPort {
  /**
     * Crea un usuario seleccionando automáticamente la estrategia adecuada
     */
    Mono<LoanApplication> create(LoanApplication loanApplication);
    
    /**
     * Crea un usuario utilizando la estrategia simple
     */
    Mono<LoanApplication> processLoanApplication(LoanApplication loanApplication);
    
    /**
     * Crea un usuario utilizando la estrategia resiliente
     */
    Mono<LoanApplication> processLoanApplicationWithResilience(LoanApplication loanApplication);
    
    /**
     * Crea un usuario utilizando una estrategia específica
     */
    Mono<LoanApplication> processLoanApplicationWithStrategy(LoanApplication loanApplication, CrearStrategyEnum strategy);
    
    /**
     * Crea múltiples usuarios en una única transacción
     */
    Mono<Void> createMultipleLoan(List<LoanApplication> loanApplications);
    
    /**
     * Crea dos usuarios en una única transacción
     */
    Mono<Void> createTwoLoans(LoanApplication loanApplication1, LoanApplication loanApplication2);
}
