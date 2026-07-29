package FlagAssessment.Prabhav.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class FlagRequest {

    @NotBlank
    private String name;

    private boolean enabled;

    @Min(0)
    @Max(100)
    private Integer rolloutPercentage;

    private Set<String> targetedUsers;

    private boolean defaultValue;
}