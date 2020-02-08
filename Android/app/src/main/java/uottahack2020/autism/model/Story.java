package uottahack2020.autism.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Story implements Roadblock {
    private Emoji avatar;
    private String situationText;
    private List<Option> options;

    private Option selectedOption;

    protected Story(Emoji avatar, String situationText, Option... options) {
        this.avatar = avatar;
        this.situationText = situationText;
        this.options = new ArrayList<>(Arrays.asList(options));
    }

    public Emoji getAvatar() {
        return avatar;
    }

    public String getSituationText() {
        return situationText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public class Option {
        private boolean visible;
        private boolean isCorrect;
        private String reasoning;

        private Option(boolean isCorrect, String reasoning) {
            this.isCorrect = isCorrect;
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
