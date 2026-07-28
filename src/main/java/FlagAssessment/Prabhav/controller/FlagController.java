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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flags")
public class FlagController {

    private final FlagService featureFlagService;
    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<FlagResponse> create(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Valid @RequestBody FlagRequest request) {

        FlagResponse response = featureFlagService.create(tenantId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<FlagResponse>> getAll(
            @RequestHeader("X-Tenant-ID") String tenantId) {

        return ResponseEntity.ok(
                featureFlagService.getAll(tenantId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlagResponse> update(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Valid @RequestBody FlagRequest request) {

        FlagResponse response =
                featureFlagService.update(id, tenantId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-ID") String tenantId) {

        featureFlagService.delete(id, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/eval")
    public ResponseEntity<EvaluationResponse> evaluate(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam String flag,
            @RequestParam String user) {

        EvaluationResponse response = evaluationService.evaluate(tenantId, flag, user);

        return ResponseEntity.ok(response);
    }
}