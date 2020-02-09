package uottahack2020.autism;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentManager;
import uottahack2020.autism.fragment.ConversationFragment;
import uottahack2020.autism.fragment.Fragment;
import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.fragment.FragmentId;
import uottahack2020.autism.fragment.QuestFragment;
import uottahack2020.autism.fragment.StoryFragment;
import uottahack2020.autism.model.DefaultQuest;

public class MainActivity extends FragmentActivity {
    public static final String TAG = "MainActivity";
    static final int LAYOUT_ID = R.layout.activity_main;
    static final int FRAGMENT_ID = R.id.main_fragment;

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityId().getLayoutId());
        saveInstance(getActivityId(), this);

        Log.d(TAG, "Launching");

        QuestFragment.setupId(getActivityId());
        StoryFragment.setupId(getActivityId());
        ConversationFragment.setupId(getActivityId());

        DefaultQuest defaultQuest = new DefaultQuest();
        defaultQuest.init();

        ProgressBar progressBar = findViewById(R.id.main_progressBar);
        progressBar.setProgress(10);
        defaultQuest.setProgressBar(progressBar);

        Session.CURRENT_QUEST = defaultQuest;

        pushFragment(FragmentId.GET(QuestFragment.TAG));
    }

    @Override
    protected ActivityId getActivityId() {
        return ActivityId.SET(this.getClass(), TAG, LAYOUT_ID);
    }

    @Override
    public void pushFragment(FragmentId fragmentId, Object... args) {
        Fragment fragment;
        try {
            fragment = fragmentId.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        fragmentStack.push(fragment);
        fragmentManager.beginTransaction()
                .replace(FRAGMENT_ID, currentFragment())
                .addToBackStack(String.valueOf(fragmentId))
                .commit();
    }

    @Override
    public void popFragment(FragmentId fragmentId) {
        try {
            fragmentManager.popBackStack(
                    String.valueOf(fragmentId), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentStack.pop().destroy();
        } catch (Exception ignored) {
//            FragmentActivity.getSavedInstance(ActivityId.MAIN_ACTIVITY).popFragment(fragmentId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("=== " + TAG + " destroyed");
    }
}
