package co.com.bancolombia.model.client.gateways;

import reactor.core.publisher.Mono;

/**
 * Puerto para la comunicación con microservicios externos
 */
public interface ExternalServiceClient {
    
    /**
     * Realiza una petición a un microservicio externo
     * @param requestData los datos de la petición
     * @return respuesta del microservicio externo
     */
    <T, R> Mono<R> callExternalService(String servicePath, T requestData, Class<R> responseType);
    
    /**
     * Realiza una petición GET a un microservicio externo
     * @param servicePath la ruta del servicio
     * @param responseType el tipo de respuesta esperado
     * @return respuesta del microservicio externo
     */
    <R> Mono<R> getFromExternalService(String servicePath, Class<R> responseType);
}
