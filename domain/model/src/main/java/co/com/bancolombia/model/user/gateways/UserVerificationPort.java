package co.com.bancolombia.model.user.gateways;

import reactor.core.publisher.Mono;

/**
 * Puerto para verificar usuarios
 */
public interface UserVerificationPort {
    
    /**
     * Verifica si un usuario existe y está activo
     * @param email Email del usuario a verificar
     * @return true si el usuario existe y está activo
     */
    Mono<Boolean> verifyUserExists(String email);
    
    /**
     * Obtiene la calificación crediticia de un usuario
     * @param email Email del usuario
     * @return calificación crediticia (0-100)
     */
    Mono<Integer> getUserCreditScore(String email);
}
