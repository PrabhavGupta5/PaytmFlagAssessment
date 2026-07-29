package FlagAssessment.Prabhav.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false)
    private boolean enabled;

    /*
     * 0-100
     * Percentage of users eligible
     */
    @Column(nullable = false)
    private Integer rolloutPercentage;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "targeted_users",
            joinColumns = @JoinColumn(name = "flag_id")
    )
    @Column(name = "username")
    @Builder.Default
    private Set<String> targetedUsers = new HashSet<>();

    @Column(nullable = false)
    private boolean defaultValue;

    /*
     * Optimistic Locking
     */
    @Version
    private Long version;
}