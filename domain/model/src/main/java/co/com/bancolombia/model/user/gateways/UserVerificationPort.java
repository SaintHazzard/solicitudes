package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
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
    Mono<User> verifyUserExists(String email);
    
}
