package FlagAssessment.Prabhav.service;

import FlagAssessment.Prabhav.entity.Flag;

import java.util.List;

public interface FlagService {
    Flag createOrUpdate(String projectId, Flag flag);
    Flag getFlag(String projectId, String flagName);
    List<Flag> getAllFlags(String projectId);
    void deleteFlag(String projectId, String flagName);
    boolean evaluate(String projectId, String flagName, String userId);
}
