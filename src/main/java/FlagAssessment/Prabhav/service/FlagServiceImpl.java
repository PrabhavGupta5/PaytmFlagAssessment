package FlagAssessment.Prabhav.service;

import FlagAssessment.Prabhav.DTO.FlagRequest;
import FlagAssessment.Prabhav.DTO.FlagResponse;
import FlagAssessment.Prabhav.Exception.DuplicateFlagException;
import FlagAssessment.Prabhav.Exception.ResourceNotFoundException;
import FlagAssessment.Prabhav.entity.Flag;
import FlagAssessment.Prabhav.repository.FlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlagServiceImpl implements FlagService {

    private final FlagRepository repository;

    @Override
    public FlagResponse create(String tenantId, FlagRequest request) {

        if (repository.existsByTenantIdAndName(tenantId, request.getName())) {
            throw new DuplicateFlagException("Flag already exists");
        }

        Flag flag = Flag.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .enabled(request.isEnabled())
                .rolloutPercentage(request.getRolloutPercentage())
                .targetedUsers(
                        request.getTargetedUsers() == null ?
                                new HashSet<>() :
                                request.getTargetedUsers())
                .defaultValue(request.isDefaultValue())
                .build();

        return map(repository.save(flag));
    }

    @Override
    public List<FlagResponse> getAll(String tenantId) {

        return repository.findAllByTenantId(tenantId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public FlagResponse update(Long id,
                               String tenantId,
                               FlagRequest request) {

        Flag flag = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Flag not found"));

        if (!flag.getName().equals(request.getName())
                && repository.existsByTenantIdAndName(tenantId, request.getName())) {
            throw new DuplicateFlagException("Flag already exists");
        }
        flag.setRolloutPercentage(request.getRolloutPercentage());
        flag.setTargetedUsers(request.getTargetedUsers());

        return map(repository.save(flag));
    }

    @Override
    public void delete(Long id, String tenantId) {
        Flag flag = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Flag not found"));

        repository.delete(flag);
    }

    private FlagResponse map(Flag flag) {

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