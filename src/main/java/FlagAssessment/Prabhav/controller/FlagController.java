package FlagAssessment.Prabhav.controller;

import FlagAssessment.Prabhav.DTO.EvaluationResponse;
import FlagAssessment.Prabhav.DTO.FlagRequest;
import FlagAssessment.Prabhav.DTO.FlagResponse;
import FlagAssessment.Prabhav.service.EvaluationService;
import FlagAssessment.Prabhav.service.FlagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlagController {

    private final FlagService featureFlagService;

    private final EvaluationService evaluationService;

    @PostMapping("/flags")
    public FlagResponse create(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody @Valid FlagRequest request) {

        return featureFlagService.create(tenantId, request);
    }

    @GetMapping("/flags")
    public List<FlagResponse> getAll(
            @RequestHeader("X-Tenant-ID") String tenantId) {

        return featureFlagService.getAll(tenantId);
    }

    @PutMapping("/flags/{id}")
    public FlagResponse update(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody FlagRequest request) {

        return featureFlagService.update(id, tenantId, request);
    }

    @DeleteMapping("/flags/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-ID") String tenantId) {

        featureFlagService.delete(id, tenantId);
    }

    @GetMapping("/eval")
    public EvaluationResponse evaluate(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam String flag,
            @RequestParam String user) {

        return evaluationService.evaluate(tenantId, flag, user);
    }
}