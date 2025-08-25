package co.com.bancolombia.model.loanApplication;

import java.math.BigDecimal;

import co.com.bancolombia.model.loanType.LoanType;
import co.com.bancolombia.model.state.State;
import lombok.Data;

@Data
public class LoanApplication {
  private String id;
  private BigDecimal value;
  private String loanTerm;
  private String email;
  private String stateId;
  private String loanTypeId;
  private String estadoId;
  private State estado;
  private LoanType loanType;
}
