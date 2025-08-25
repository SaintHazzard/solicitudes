package co.com.bancolombia.r2dbc.implementaciones.loan;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import co.com.bancolombia.model.common.CrearStrategy;
import co.com.bancolombia.model.common.CrearStrategyEnum;
import co.com.bancolombia.model.helpers.MultiDecoratorAbstractFactory;
import co.com.bancolombia.model.loanApplication.LoanApplication;
import co.com.bancolombia.model.loanApplication.gateways.StrategyLoanFactory;

/**
 * Implementación específica para CrearUsuarioStrategy que implementa
 * StrategyFactory
 * Utiliza la fábrica genérica MultiDecoratorAbstractFactory
 */
@Service
public class LoanCreationStrategyFactory implements StrategyLoanFactory {

    private final MultiDecoratorAbstractFactory<CrearStrategy<LoanApplication>, CrearStrategyEnum> factory;

    public LoanCreationStrategyFactory(
            List<CrearStrategy<LoanApplication>> strategies,
            List<Function<CrearStrategy<LoanApplication>, CrearStrategy<LoanApplication>>> decorators) {

        this.factory = new MultiDecoratorAbstractFactory<>(
                strategies,
                decorators,
                CrearStrategy::getType);

        this.factory.init();
    }

    @Override
    public CrearStrategy<LoanApplication> getStrategy(CrearStrategyEnum type) {
        return factory.getStrategy(type);
    }
}
