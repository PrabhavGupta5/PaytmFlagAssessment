package FlagAssessment.Prabhav.DTO;

import FlagAssessment.Prabhav.entity.Flag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
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
    private Long version;

    public Flag toEntity(String tenantId) {
        return Flag.builder()
                .tenantId(tenantId)
                .name(name)
                .enabled(enabled)
                .rolloutPercentage(rolloutPercentage)
                .targetedUsers(
                        targetedUsers == null
                                ? new HashSet<>()
                                : targetedUsers
                )
                .defaultValue(defaultValue)
                .version(version)
                .build();
    }
}