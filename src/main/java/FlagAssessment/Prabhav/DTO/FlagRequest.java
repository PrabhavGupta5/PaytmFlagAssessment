package FlagAssessment.Prabhav.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlagRequest {

    @NotBlank
    private String name;

    private boolean enabled;

    private boolean defaultValue;
}