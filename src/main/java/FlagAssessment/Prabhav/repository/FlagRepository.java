package FlagAssessment.Prabhav.repository;

import FlagAssessment.Prabhav.entity.Flag;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FlagRepository {
    // projectId -> (flagName -> Flag)
    private final Map<String, Map<String, Flag>> store = new ConcurrentHashMap<>();

    public void save(String projectId, Flag flag) {
        store.computeIfAbsent(projectId, k -> new ConcurrentHashMap<>())
                .put(flag.getName(), flag);
    }

    public Optional<Flag> findByName(String projectId, String flagName) {
        Map<String, Flag> projectFlags = store.get(projectId);
        if (projectFlags == null) return Optional.empty();
        return Optional.ofNullable(projectFlags.get(flagName));
    }

    public List<Flag> findAllByProject(String projectId) {
        Map<String, Flag> projectFlags = store.get(projectId);
        return projectFlags == null ? Collections.emptyList() : new ArrayList<>(projectFlags.values());
    }

    public boolean deleteByName(String projectId, String flagName) {
        Map<String, Flag> projectFlags = store.get(projectId);
        if (projectFlags == null) return false;
        return projectFlags.remove(flagName) != null;
    }

    public boolean projectExists(String projectId) {
        return store.containsKey(projectId);
    }

    public void clear() { // for tests
        store.clear();
    }
}