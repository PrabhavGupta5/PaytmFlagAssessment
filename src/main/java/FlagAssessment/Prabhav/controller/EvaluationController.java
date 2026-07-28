package FlagAssessment.Prabhav.controller;


import FlagAssessment.Prabhav.service.FlagService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}")
public class EvaluationController {

    private final FlagService flagService;

    public EvaluationController(FlagService flagService) {
        this.flagService = flagService;
    }

    @GetMapping("/evaluate")
    public Map<String, Boolean> evaluate(@PathVariable String projectId,
                                         @RequestParam String flag,
                                         @RequestParam String user) {
        boolean result = flagService.evaluate(projectId, flag, user);
        return Map.of("enabled", result);
    }
}