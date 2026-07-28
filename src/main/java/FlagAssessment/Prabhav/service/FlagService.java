package FlagAssessment.Prabhav.service;

import FlagAssessment.Prabhav.DTO.FlagRequest;
import FlagAssessment.Prabhav.DTO.FlagResponse;

import java.util.List;


public interface FlagService {

    FlagResponse create(String tenantId, FlagRequest request);

    List<FlagResponse> getAll(String tenantId);

    FlagResponse update(Long id,
                        String tenantId,
                        FlagRequest request);

    void delete(Long id,
                String tenantId);

}
