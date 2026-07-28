package FlagAssessment.Prabhav.service;


import FlagAssessment.Prabhav.entity.Flag;
import FlagAssessment.Prabhav.repository.FlagRepository;

import FlagAssessment.Prabhav.strategy.SimpleEvaluationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock
    private FlagRepository repository;

    private final SimpleEvaluationStrategy strategy =
            new SimpleEvaluationStrategy();

    @InjectMocks
    private EvaluationServiceImpl service;

    @Test
    void shouldEvaluateEnabledFlag(){

        service = new EvaluationServiceImpl(repository,strategy);

        Flag flag = Flag.builder()
                .tenantId("tenant1")
                .name("NEW_UI")
                .enabled(true)
                .defaultValue(false)
                .build();

        when(repository.findByTenantIdAndName("tenant1","NEW_UI"))
                .thenReturn(Optional.of(flag));

        assertTrue(
                service.evaluate("tenant1",
                        "NEW_UI",
                        "123").isEnabled()
        );
    }

}