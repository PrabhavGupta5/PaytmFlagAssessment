package FlagAssessment.Prabhav.strategy;

import FlagAssessment.Prabhav.entity.Flag;
import org.springframework.stereotype.Component;

@Component
public class SimpleEvaluationStrategy implements EvaluationStrategy {

    @Override
    public boolean evaluate(Flag featureFlag, String user) {

        return featureFlag.isEnabled();
    }
}