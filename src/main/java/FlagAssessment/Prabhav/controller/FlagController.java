package FlagAssessment.Prabhav.controller;

import FlagAssessment.Prabhav.entity.Flag;
import FlagAssessment.Prabhav.service.FlagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/flags")
public class FlagController {

    private final FlagService flagService;

    public FlagController(FlagService flagService) {
        this.flagService = flagService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Flag createOrUpdateFlag(@PathVariable String projectId,
                                   @Valid @RequestBody Flag flag) {
        return flagService.createOrUpdate(projectId, flag);
    }

    @GetMapping("/{flagName}")
    public Flag getFlag(@PathVariable String projectId,
                        @PathVariable String flagName) {
        return flagService.getFlag(projectId, flagName);
    }

    @GetMapping
    public List<Flag> getAllFlags(@PathVariable String projectId) {
        return flagService.getAllFlags(projectId);
    }

    @DeleteMapping("/{flagName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlag(@PathVariable String projectId,
                           @PathVariable String flagName) {
        flagService.deleteFlag(projectId, flagName);
    }
}