package uottahack2020.autism.controller;

import android.view.View;

import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.model.DefaultQuest;
import uottahack2020.autism.model.Quest;

public class QuestCtrl implements FragmentCtrl {
    private FragmentActivity activity;
    private Quest quest;

    public QuestCtrl(FragmentActivity activity) {
        this.activity = activity;
        this.quest = new DefaultQuest();
    }

    @Override
    public void init(View view) {
        quest.init();
    }

    @Override
    public void updateInfo() {

    }
}
