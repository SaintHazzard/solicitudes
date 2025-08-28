package co.com.bancolombia.r2dbc.implementaciones.loan;

import org.springframework.stereotype.Service;

import co.com.bancolombia.model.common.ReactiveTx;
import co.com.bancolombia.model.loanApplication.LoanApplication;
import co.com.bancolombia.model.loanApplication.gateways.CreateLoanPort;
import co.com.bancolombia.model.loanApplication.gateways.LoanValidationAndCreationPort;
import co.com.bancolombia.model.user.gateways.UserVerificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para validar un usuario y luego crear un préstamo
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ValidateAndCreateLoan implements LoanValidationAndCreationPort {

    private final UserVerificationPort userVerificationPort;
    private final CreateLoanPort createLoanPort;
    private final ReactiveTx reactiveTx;

    /**
     * Verifica si el usuario existe antes de crear el préstamo
     * 
     * @param loanApplication Solicitud de préstamo
     * @return Solicitud de préstamo creada o error si el usuario no existe
     */
    public Mono<LoanApplication> validateUserAndCreateLoan(LoanApplication loanApplication) {
        return reactiveTx.write(() -> userVerificationPort.verifyUserExists(loanApplication.getEmail())
                .flatMap(userExists -> {
                    log.info("Usuario existe: {}", userExists);
                    if (userExists == null) {
                        return Mono.error(new IllegalArgumentException("El usuario no existe"));
                    } else if (loanApplication.getEmail() != null
                            && loanApplication.getEmail().equals(userExists.getEmail())) {
                        return createLoanPort.processLoanApplicationWithResilience(loanApplication);
                    }
                    return Mono.error(new IllegalArgumentException("La solicitud de préstamo no es válida"));
                }));
    }
}
