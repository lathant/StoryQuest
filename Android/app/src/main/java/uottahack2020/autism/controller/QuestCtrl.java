package uottahack2020.autism.controller;

import android.view.View;

import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.fragment.FragmentId;
import uottahack2020.autism.model.ActionPoint;
import uottahack2020.autism.model.Emoji;
import uottahack2020.autism.model.Quest;
import uottahack2020.autism.model.Roadblock;
import uottahack2020.autism.model.Story;

public class QuestCtrl implements FragmentCtrl {
    private FragmentActivity activity;
    private Quest quest;

    public QuestCtrl(FragmentActivity activity) {
        this.activity = activity;
        this.quest = setupQuest();
    }

    @Override
    public void init(View view) {

    }

    @Override
    public void updateInfo() {

    }

    private static Quest setupQuest() {
        return null;
    }
}
