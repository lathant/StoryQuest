package uottahack2020.autism.model;

import uottahack2020.autism.ActivityId;
import uottahack2020.autism.fragment.FragmentId;

public abstract class Roadblock {
    private String situationText;
    protected boolean completed;

    Roadblock(String situationText) {
        this.situationText = situationText;
    }

    public abstract FragmentId getFragmentId(ActivityId activityId);

    public abstract String getName();

    public abstract String getDescription();

    public String getSituationText() {
        return situationText;
    }

    public boolean isComplete() {
        return completed;
    }

    public void forceComplete() {
        completed = true;
    }
}
