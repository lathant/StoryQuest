package uottahack2020.autism.controller;

import android.view.View;

import uottahack2020.autism.R;
import uottahack2020.autism.fragment.ConversationFragment;
import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.fragment.FragmentId;
import uottahack2020.autism.fragment.StoryFragment;
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
        view.findViewById(R.id.quest_btnPuzzle1).setOnClickListener(v -> {
            activity.pushFragment(FragmentId.GET(StoryFragment.TAG), R.anim.trans_bottom_in, R.anim.trans_top_out, R.anim.trans_top_in, R.anim.trans_bottom_out);
        });
        view.findViewById(R.id.quest_btnPuzzle2).setOnClickListener(v -> {
            activity.pushFragment(FragmentId.GET(ConversationFragment.TAG), R.anim.trans_bottom_in, R.anim.trans_top_out, R.anim.trans_top_in, R.anim.trans_bottom_out);
        });
        view.findViewById(R.id.quest_btnPuzzle3).setOnClickListener(v -> {
//          activity.pushFragment(FragmentId.GET(ConversationFragment.TAG));
//            activity.overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_top_out);
        });

        quest.init();
    }

    @Override
    public void updateInfo() {

    }
}
