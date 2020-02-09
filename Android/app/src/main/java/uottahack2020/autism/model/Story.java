package uottahack2020.autism.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Story extends Roadblock {
    private Emoji avatar;
    private List<Option> options;

    private Option selectedOption;

    public Option getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }

    protected Story(String situationText, Emoji avatar, Option... options) {
        super(situationText);
        this.avatar = avatar;
        this.options = new ArrayList<>(Arrays.asList(options));
    }

    public Emoji getAvatar() {
        return avatar;
    }

    public List<Option> getOptions() {
        return options;
    }

    public static class Option {
        private boolean visible;
        private boolean isCorrect;
        private String text;
        private String reasoning;

        Option(boolean isCorrect, String text, String reasoning) {
            this.isCorrect = isCorrect;
            this.text = text;
            this.reasoning = reasoning;
        }

        public boolean isVisible() {
            return visible;
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
