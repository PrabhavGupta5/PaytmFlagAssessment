package FlagAssessment.Prabhav.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        name = "feature_flags",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "tenant_id",
                                "name"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="tenant_id",nullable=false)
    private String tenantId;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private boolean enabled;

    @Column(nullable=false)
    private boolean defaultValue;   // used when state == DEFAULT
}