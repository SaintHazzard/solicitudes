package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanHandlerRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(LoanHandler handler) {
        return route(GET("/api/usecase/path"), handler::listenGETUseCase)
                .andRoute(POST("/api/v1/solicitudes"), handler::createLoanWithUserValidation)
                .and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase));
    }
}
