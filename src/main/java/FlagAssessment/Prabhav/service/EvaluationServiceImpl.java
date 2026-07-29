package FlagAssessment.Prabhav.service;


import FlagAssessment.Prabhav.DTO.EvaluationResponse;
import FlagAssessment.Prabhav.Exception.ResourceNotFoundException;
import FlagAssessment.Prabhav.entity.Flag;
import FlagAssessment.Prabhav.repository.FlagRepository;
import FlagAssessment.Prabhav.strategy.EvaluationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final FlagRepository repository;

    private final EvaluationStrategy strategy;

    @Override
    public EvaluationResponse evaluate(String tenantId, String flag, String user) {

        Flag featureFlag = repository.findByTenantIdAndName(tenantId, flag)
                        .orElseThrow(() -> new ResourceNotFoundException("Flag not found"));

        boolean enabled = strategy.evaluate(featureFlag, user);

        return EvaluationResponse.builder()
                .flag(flag)
                .user(user)
                .enabled(enabled)
                .build();
    }
}