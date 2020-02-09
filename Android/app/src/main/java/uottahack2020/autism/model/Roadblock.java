package uottahack2020.autism.model;

import uottahack2020.autism.ActivityId;
import uottahack2020.autism.fragment.FragmentId;

public abstract class Roadblock {
    private Quest quest;
    private String situationText;
    private boolean completed;

    Roadblock(Quest quest, String situationText) {
        this.quest = quest;
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
        if (completed) return;
        completed = true;
        quest.advance();
    }
}
