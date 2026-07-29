package FlagAssessment.Prabhav.strategy;

import FlagAssessment.Prabhav.entity.Flag;
import org.springframework.stereotype.Component;

@Component
public class SimpleEvaluationStrategy implements EvaluationStrategy {

    @Override
    public boolean evaluate(Flag featureFlag, String user) {
        /*
         * Flag globally disabled
         */
        if (!featureFlag.isEnabled()) {
            return featureFlag.isDefaultValue();
        }

        /*
         * Explicit targeting
         */
        if (featureFlag.getTargetedUsers() != null && featureFlag.getTargetedUsers().contains(user)) {
            return true;
        }

        /*
         * Stable percentage rollout
         */
        int bucket = Math.abs(user.hashCode()) % 100;

        return bucket < featureFlag.getRolloutPercentage();
    }
}