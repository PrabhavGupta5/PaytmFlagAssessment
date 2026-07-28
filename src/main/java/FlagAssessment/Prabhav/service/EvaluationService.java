package FlagAssessment.Prabhav.service;

import FlagAssessment.Prabhav.DTO.EvaluationResponse;

public interface EvaluationService {
    EvaluationResponse evaluate(String tenantId, String flag, String user);
}