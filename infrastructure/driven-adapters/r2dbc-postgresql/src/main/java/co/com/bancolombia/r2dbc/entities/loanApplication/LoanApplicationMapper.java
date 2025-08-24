package co.com.bancolombia.r2dbc.entities.loanApplication;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import co.com.bancolombia.model.loanApplication.LoanApplication;
import co.com.bancolombia.r2dbc.entities.dto.LoanAppliDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanApplicationMapper {

  LoanAppliDTO toDto(LoanApplication entity);

  LoanApplicationEntity toEntity(LoanApplication dto);

  LoanApplicationEntity toEntity(LoanAppliDTO dto);



  LoanApplication toDomain(LoanApplicationEntity entity);

}
