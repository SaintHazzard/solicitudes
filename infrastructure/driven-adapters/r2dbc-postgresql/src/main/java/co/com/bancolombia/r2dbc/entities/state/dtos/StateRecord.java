package co.com.bancolombia.r2dbc.entities.state.dtos;


import jakarta.validation.constraints.NotBlank;

public record StateRecord(
    String id,
    @NotBlank String name,
    @NotBlank String description) {

}
