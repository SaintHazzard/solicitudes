package co.com.bancolombia.usecase.loan.externalservice;

/**
 * Clase para la respuesta del API externo
 */
public record ExternalApiResponse(boolean approved, String message) {}
