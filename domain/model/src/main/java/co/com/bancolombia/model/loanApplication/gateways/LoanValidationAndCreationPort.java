package co.com.bancolombia.model.loanApplication.gateways;

import co.com.bancolombia.model.loanApplication.LoanApplication;
import reactor.core.publisher.Mono;

public interface LoanValidationAndCreationPort {
    Mono<LoanApplication> validateUserAndCreateLoan(LoanApplication loanApplication);
}
