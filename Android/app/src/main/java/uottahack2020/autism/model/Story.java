package uottahack2020.autism.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Story extends Roadblock {
    private Emoji avatar;
    private List<Option> options;

    private Option selectedOption;

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

    public class Option {
        private boolean visible;
        private boolean isCorrect;
        private String text;
        private String reasoning;

        Option(boolean isCorrect, String text, String reasoning) {
            this.isCorrect = isCorrect;
            this.text = text;
            this.reasoning = reasoning;
        }

        public boolean isSelected() {
            return selectedOption == this;
        }

        public boolean select() {
            selectedOption = this;
            return isCorrect;
        }

        public String getReasoning() {
            return reasoning;
        }
    }

}
