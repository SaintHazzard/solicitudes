package co.com.bancolombia.r2dbc.entities.loanType;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Table("loan_types")
public class LoanTypeEntity {

  @Id
  private String id;

  @NotBlank
  private String name;

  @Digits(integer = 10, fraction = 2)
  @Min(value = 0)
  private BigDecimal minValue;

  @Digits(integer = 10, fraction = 2)
  @Min(value = 0)
  private BigDecimal maxValue;

  @Digits(integer = 5, fraction = 2)
  @Min(value = 0)
  private BigDecimal interestRate;


  private String stateId;

  private String loanTypeId;
}
