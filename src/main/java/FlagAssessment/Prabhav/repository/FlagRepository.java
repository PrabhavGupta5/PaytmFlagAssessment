package FlagAssessment.Prabhav.repository;


import FlagAssessment.Prabhav.entity.Flag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlagRepository extends JpaRepository<Flag, Long> {

    List<Flag> findAllByTenantId(String tenantId);

    Optional<Flag> findByIdAndTenantId(Long id, String tenantId);

    Optional<Flag> findByTenantIdAndName(String tenantId, String name);

    boolean existsByTenantIdAndName(String tenantId, String name);

}