package co.com.bancolombia.usecase.loan.externalservice;

import co.com.bancolombia.model.client.gateways.ExternalServiceClient;
import co.com.bancolombia.model.loanApplication.LoanApplication;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Caso de uso que demuestra cómo llamar a un servicio externo
 */
@RequiredArgsConstructor
public class ExternalServiceUseCase {
    
    private final ExternalServiceClient externalServiceClient;
    
    /**
     * Ejemplo de llamada a servicio externo
     */
    public Mono<ExternalApiResponse> validateLoanWithExternalService(LoanApplication loanApplication) {
        // Crea el objeto de petición
        ExternalApiRequest request = new ExternalApiRequest(
            loanApplication.getId(),
            loanApplication.getValue()
        );
        
        // Llama al servicio externo
        return externalServiceClient.callExternalService(
            "/api/loan-validation", 
            request, 
            ExternalApiResponse.class
        );
    }
}
