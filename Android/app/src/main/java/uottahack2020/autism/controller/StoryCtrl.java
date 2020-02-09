package uottahack2020.autism.controller;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import uottahack2020.autism.R;
import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.fragment.FragmentId;
import uottahack2020.autism.model.Story;

public class StoryCtrl implements FragmentCtrl {
    private FragmentActivity activity;
    private String fragmentTag;
    private Story story;

    private TextView txtEmoji;
    private TextView txtSituation;
    private TextView txtHint;
    private Button btnOption1;
    private Button btnOption2;
    private Button btnOption3;
    private Button btnOption4;
    private View panelHint;

    public StoryCtrl(FragmentActivity activity, String fragmentTag) {
        this.activity = activity;
        this.fragmentTag = fragmentTag;
    }

    @Override
    public void init(View view) {
        txtEmoji = view.findViewById(R.id.story_txtEmoji);
        txtSituation = view.findViewById(R.id.story_txtSituation);
        txtHint = view.findViewById(R.id.story_txtHint);
        btnOption1 = view.findViewById(R.id.story_btnOption1);
        btnOption2 = view.findViewById(R.id.story_btnOption2);
        btnOption3 = view.findViewById(R.id.story_btnOption3);
        btnOption4 = view.findViewById(R.id.story_btnOption4);
        panelHint = view.findViewById(R.id.story_panelHint);

        panelHint.setVisibility(View.INVISIBLE);

        btnOption1.setOnClickListener(v -> handleOptionSelect(1));
        btnOption2.setOnClickListener(v -> handleOptionSelect(2));
        btnOption3.setOnClickListener(v -> handleOptionSelect(3));
        btnOption4.setOnClickListener(v -> handleOptionSelect(4));
    }

    @Override
    public void updateInfo() {
        txtEmoji.setText(story.getAvatar().getEmoji());
        txtSituation.setText(story.getSituationText());

        List<Story.Option> options = story.getOptions();

        btnOption1.setVisibility(options.get(0).isVisible() ? View.VISIBLE : View.INVISIBLE);
        btnOption2.setVisibility(options.get(1).isVisible() ? View.VISIBLE : View.INVISIBLE);
        btnOption3.setVisibility(options.get(2).isVisible() ? View.VISIBLE : View.INVISIBLE);
        btnOption4.setVisibility(options.get(3).isVisible() ? View.VISIBLE : View.INVISIBLE);

        btnOption1.setText(options.get(0).getText());
        btnOption2.setText(options.get(1).getText());
        btnOption3.setText(options.get(2).getText());
        btnOption4.setText(options.get(3).getText());
    }

    public void setStory(Story story) {
        this.story = story;
    }

    private void handleOptionSelect(int n) {
        Story.Option option = story.getOption(n);
        if (!story.setSelectedOption(option)) {
            option.setInvisible();
            txtHint.setText(option.getReasoning());
            panelHint.setVisibility(View.VISIBLE);
        } else {
            option.getStory().forceComplete();
            activity.popFragment(FragmentId.GET(fragmentTag));
        }
        updateInfo();
    }
}
