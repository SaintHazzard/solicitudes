package co.com.bancolombia.r2dbc.entities.dto;

import java.math.BigDecimal;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LoanAppliDTO(

    String id,

    @Min(value = 0) BigDecimal value,

    @NotBlank String loanTerm,

    @Email @NotBlank String email,

    @NotBlank String stateId,

    @NotBlank String loanTypeId,

    @NotBlank String estadoId) {

}
