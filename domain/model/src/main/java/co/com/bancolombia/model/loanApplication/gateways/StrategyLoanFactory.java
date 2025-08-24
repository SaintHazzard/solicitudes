package co.com.bancolombia.model.loanApplication.gateways;


import co.com.bancolombia.model.common.CrearStrategy;
import co.com.bancolombia.model.common.CrearStrategyEnum;
import co.com.bancolombia.model.loanApplication.LoanApplication;

/**
 * Interfaz común para todas las fábricas de estrategias
 */
public interface StrategyLoanFactory {

  /**
   * Obtiene una estrategia por su tipo
   */
  CrearStrategy<LoanApplication> getStrategy(CrearStrategyEnum type);
}
