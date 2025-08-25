package co.com.bancolombia.api;

import co.com.bancolombia.model.loanApplication.LoanApplication;
import co.com.bancolombia.model.loanApplication.gateways.LoanValidationAndCreationPort;
import co.com.bancolombia.r2dbc.entities.loanApplication.LoanApplicationMapper;
import co.com.bancolombia.usecase.loan.externalservice.ExternalServiceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanHandler {
    private final ExternalServiceUseCase externalServiceUseCase;
    private final LoanValidationAndCreationPort validateAndCreateLoanUseCase;
    private final LoanApplicationMapper loanMapper;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("Hello from GET");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("Hello from GET Other");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanApplication.class)
                .flatMap(externalServiceUseCase::validateLoanWithExternalService)
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result));
    }

    /**
     * Endpoint para crear un préstamo con validación de usuario
     */
    public Mono<ServerResponse> createLoanWithUserValidation(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanApplication.class)
                .flatMap(validateAndCreateLoanUseCase::validateUserAndCreateLoan)
                .map(loanMapper::toDto)
                .flatMap(loan -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(loan))
                .onErrorResume(IllegalArgumentException.class, error -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(error.getMessage())));
    }

    record ErrorResponse(String error) {
    }
}
