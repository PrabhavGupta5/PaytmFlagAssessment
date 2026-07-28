package FlagAssessment.Prabhav.strategy;

import FlagAssessment.Prabhav.entity.Flag;

public interface EvaluationStrategy {
    boolean evaluate(Flag featureFlag, String user);
}