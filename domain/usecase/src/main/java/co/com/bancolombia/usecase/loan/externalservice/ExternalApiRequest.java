package co.com.bancolombia.usecase.loan.externalservice;

/**
 * Clase para la petición al API externo
 */
public record ExternalApiRequest(String loanId, Object loanValue) {}
