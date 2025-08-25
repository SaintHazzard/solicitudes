package co.com.bancolombia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {


//         @Bean
//   public StrategyLoanFactory loanCreationStrategyFactory(
//       List<CrearStrategy<LoanApplication>> strategies,
//       ReactiveTx reactiveTx
//       ) {

//     List<Function<CrearStrategy<LoanApplication>, CrearStrategy<LoanApplication>>> decorators = new ArrayList<>();

    
//     // 4. Finalmente aplicamos logging (para registrar después de todas las validaciones y transacciones)
//     decorators.add(strategy -> new LoggingCrearUsuarioDecorator(strategy, logger));

//     // Creamos la fábrica con las estrategias y los decoradores
//     return new StrategyLoanFactory(strategies, decorators);
//   }
}
