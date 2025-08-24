package co.com.bancolombia.r2dbc.entities.state;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Table("states")
public class StateEntity {

  @Id
  private String id;

  @NotBlank
  private String name;

  @NotBlank
  private String description;
}
