package FlagAssessment.Prabhav.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationResponse {

    private String flag;

    private String user;

    private boolean enabled;
}