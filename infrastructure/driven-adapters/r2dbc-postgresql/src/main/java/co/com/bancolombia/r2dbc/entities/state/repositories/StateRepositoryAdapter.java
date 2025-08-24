package co.com.bancolombia.r2dbc.entities.state.repositories;

import co.com.bancolombia.model.state.State;
import co.com.bancolombia.r2dbc.entities.state.StateEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StateRepositoryAdapter extends ReactiveAdapterOperations<
    State/* change for domain model */,
    StateEntity/* change for adapter model */,
    String,
    StateReactiveRepository
> {
    public StateRepositoryAdapter(StateReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, State.class/* change for domain model */));
    }

}
