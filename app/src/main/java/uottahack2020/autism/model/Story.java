package uottahack2020.autism.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Story extends Roadblock {
    private Emoji avatar;
    private List<Option> options;
    private Option selectedOption;

    protected Story(Quest quest, String situationText, Emoji avatar, Option... options) {
        super(quest, situationText);
        this.avatar = avatar;
        this.options = new ArrayList<>(Arrays.asList(options));
        for (Option option : this.options) {
            option.story = this;
        }
    }

    public Option getSelectedOption() {
        return selectedOption;
    }

    public boolean setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
        return selectedOption.isCorrect();
    }

    public Emoji getAvatar() {
        return avatar;
    }

    public Option getOption(int n) {
        return options.get(n - 1);
    }

    public List<Option> getOptions() {
        return options;
    }

    public static class Option {
        private Story story;
        private boolean visible;
        private boolean isCorrect;
        private String text;
        private String reasoning;

        Option(boolean isCorrect, String text, String reasoning) {
            visible = true;
            this.isCorrect = isCorrect;
            this.text = text;
            this.reasoning = reasoning;
        }

        public Story getStory() {
            return story;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setInvisible() {
            this.visible = false;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public String getText() {
            return text;
        }

        public String getReasoning() {
            return reasoning;
        }
    }

}
