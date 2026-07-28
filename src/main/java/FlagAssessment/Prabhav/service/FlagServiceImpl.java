package FlagAssessment.Prabhav.service;

import FlagAssessment.Prabhav.entity.Flag;
import FlagAssessment.Prabhav.repository.FlagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlagServiceImpl implements FlagService {

    private final FlagRepository repository;

    public FlagServiceImpl(FlagRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flag createOrUpdate(String projectId, Flag flag) {
        repository.save(projectId, flag);
        return flag;
    }


    @Override
    public Flag getFlag(String projectId, String flagName) {
        return repository.findByName(projectId, flagName)
                .orElseThrow(() -> new FlagNotFoundException(projectId, flagName));
    }

    @Override
    public List<Flag> getAllFlags(String projectId) {
        if (!repository.projectExists(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
        return repository.findAllByProject(projectId);
    }

    @Override
    public void deleteFlag(String projectId, String flagName) {
        boolean deleted = repository.deleteByName(projectId, flagName);
        if (!deleted) {
            throw new FlagNotFoundException(projectId, flagName);
        }
    }

    @Override
    public boolean evaluate(String projectId, String flagName, String userId) {
        Flag flag = repository.findByName(projectId, flagName)
                .orElseThrow(() -> new FlagNotFoundException(projectId, flagName));

        // For now, we ignore 'userId' – we treat it as stable for any user.
        // In a real system you would add targeting rules (percentage, user lists, etc.)
        return switch (flag.getState()) {
            case ON -> true;
            case OFF -> false;
            case DEFAULT -> flag.isDefaultValue();
        };
    }
}