package co.com.bancolombia.r2dbc.entities.loanApplication;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import co.com.bancolombia.model.loanType.LoanType;
import co.com.bancolombia.model.state.State;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Table("loan_applications")
public class LoanApplicationEntity {


  @Id
  private String id;


  @Min(value = 0)
  private BigDecimal value;


  @NotBlank
  @Column("loan_term")
  private String loanTerm;

  @Email
  @NotBlank
  @Column("email")
  private String email;

  @NotBlank
  @Column("state_id")
  private String stateId;

  @NotBlank
  @Column("loan_type_id")
  private String loanTypeId;

  @NotBlank
  @Column("estado_id")
  private String estadoId;


  private State estado;
  private LoanType loanType;
}
