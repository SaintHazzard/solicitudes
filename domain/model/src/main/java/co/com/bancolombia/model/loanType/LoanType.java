package co.com.bancolombia.model.loanType;

import java.math.BigDecimal;

import lombok.Data;


@Data
public class LoanType {
  private String id;
  private String name;
  private BigDecimal minValue;
  private BigDecimal maxValue;
  private BigDecimal interestRate;
  private String currency;
}
