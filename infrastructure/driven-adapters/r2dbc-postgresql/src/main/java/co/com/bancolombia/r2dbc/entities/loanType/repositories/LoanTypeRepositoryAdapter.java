package co.com.bancolombia.r2dbc.entities.loanType.repositories;

import co.com.bancolombia.model.loanType.LoanType;
import co.com.bancolombia.model.loanType.gateways.LoanTypeRepository;
import co.com.bancolombia.r2dbc.entities.loanType.LoanTypeEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<
    LoanType/* change for domain model */,
    LoanTypeEntity/* change for adapter model */,
    String,
    LoanTypeReactiveRepository
> implements LoanTypeRepository {
    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanType.class/* change for domain model */));
    }

}
