package FlagAssessment.Prabhav.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class FlagResponse {

    private Long id;

    private String name;

    private boolean enabled;

    private boolean defaultValue;

    private Integer rolloutPercentage;

    private Set<String> targetedUsers;

    private Long version;
}