package co.com.bancolombia.r2dbc.entities.state;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("states")
public class StateEntity {
  private String id;
  private String name;
  private String description;
}
