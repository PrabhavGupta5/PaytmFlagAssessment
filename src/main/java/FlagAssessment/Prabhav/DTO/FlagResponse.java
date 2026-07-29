package FlagAssessment.Prabhav.DTO;

import lombok.Builder;
import lombok.Data;
import FlagAssessment.Prabhav.entity.Flag;

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

    public static FlagResponse fromEntity(Flag flag) {
        return FlagResponse.builder()
                .id(flag.getId())
                .name(flag.getName())
                .enabled(flag.isEnabled())
                .rolloutPercentage(flag.getRolloutPercentage())
                .targetedUsers(flag.getTargetedUsers())
                .defaultValue(flag.isDefaultValue())
                .version(flag.getVersion())
                .build();

    }
}