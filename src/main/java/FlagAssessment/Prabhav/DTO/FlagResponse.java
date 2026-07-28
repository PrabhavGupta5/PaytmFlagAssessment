package FlagAssessment.Prabhav.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlagResponse {

    private Long id;

    private String name;

    private boolean enabled;

    private boolean defaultValue;
}