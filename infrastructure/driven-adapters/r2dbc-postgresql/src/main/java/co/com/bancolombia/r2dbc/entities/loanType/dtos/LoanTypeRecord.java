package co.com.bancolombia.r2dbc.entities.loanType.dtos;

import java.math.BigDecimal;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LoanTypeRecord(
    String id,

    @NotBlank String name,

    @Digits(integer = 10, fraction = 2) @Min(value = 0) BigDecimal minValue,

    @Digits(integer = 10, fraction = 2) @Min(value = 0) BigDecimal maxValue,

    @Digits(integer = 5, fraction = 2) @Min(value = 0) BigDecimal interestRate,

    String stateId,

    String loanTypeId) {
}
