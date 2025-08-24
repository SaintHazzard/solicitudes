package co.com.bancolombia.r2dbc.entities.loanType;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;


@Data
@Table("loan_types")
public class LoanTypeEntity {

  @Id
  private String id;

  
  private String name;
  private BigDecimal minValue;
  private BigDecimal maxValue;
  private BigDecimal interestRate;
  private String currency;
}
