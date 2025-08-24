package co.com.bancolombia.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;

import co.com.bancolombia.model.loanApplication.LoanApplication;
import co.com.bancolombia.r2dbc.entities.loanApplication.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.entities.loanApplication.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.entities.loanApplication.repositories.LoanApplicationReactiveRepository;
import co.com.bancolombia.r2dbc.entities.loanApplication.repositories.LoanApplicationRepositoryAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    LoanApplicationRepositoryAdapter repositoryAdapter;

    @Mock
    LoanApplicationReactiveRepository repository;

    @Mock
    LoanApplicationMapper mapper;

    @Test
    void mustFindValueById() {

        var entity = mock(LoanApplicationEntity.class);
        when(repository.findById("1")).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(mock(LoanApplication.class));

        Mono<LoanApplication> result = repositoryAdapter.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(mock(LoanApplication.class)))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {

        when(repository.findAll()).thenReturn(Flux.just(mock(LoanApplicationEntity.class)));
        when(mapper.toDomain(mock(LoanApplicationEntity.class))).thenReturn(mock(LoanApplication.class));

        Flux<LoanApplication> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(mock(LoanApplication.class)))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void mustFindByExample() {
        LoanApplicationEntity exampleEntity = mock(LoanApplicationEntity.class);
        LoanApplication loanApplication = mock(LoanApplication.class);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(exampleEntity));
        when(mapper.toDomain(exampleEntity)).thenReturn(loanApplication);

        Flux<LoanApplication> result = repositoryAdapter.findByExample(loanApplication);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(loanApplication))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        LoanApplicationEntity entity = mock(LoanApplicationEntity.class);
        LoanApplication loanApplication = mock(LoanApplication.class);
        when(mapper.toEntity(loanApplication)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(loanApplication);

        Mono<LoanApplication> result = repositoryAdapter.save(loanApplication);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(loanApplication))
                .verifyComplete();
    }
}
