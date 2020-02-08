package uottahack2020.autism.model;

import uottahack2020.autism.ActivityId;
import uottahack2020.autism.fragment.ConversationFragment;
import uottahack2020.autism.fragment.FragmentId;

public abstract class Conversation extends Roadblock {
    private static final String[] RESPONSES_TO_CORRECT = new String[]{
            "That's great! Keep going",
            "Perfect! On to the next one",
            "You're amazing! Let's do another"
    };
    private static final String[] RESPONSES_TO_INCORRECT = new String[]{
            "Hmm that's not quite right, try again",
            "Sometimes we get things wrong and it's okay, give it another go",
            "I'm loving your effort but that's not quite right"
    };

    private Question[] questions;
    private int currentQuestion;
    private Marker marker;

    Conversation(String situationText, Question... questions) {
        super(situationText);
        if (questions.length == 0) {
            throw new RuntimeException("You must provide at least 1 question");
        }
        this.questions = questions;
        currentQuestion = 0;
    }

    @Override
    public FragmentId getFragmentId(ActivityId activityId) {
        return ConversationFragment.setupId(activityId);
    }

    public Question getCurrentQuestion() {
        return questions[currentQuestion];
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String markAnswer(String answer) {
        boolean correct = marker.markAnswer(getCurrentQuestion(), answer);

        if (correct) {
            if (++currentQuestion >= questions.length) {
                completed = true;
            }
            return RESPONSES_TO_CORRECT[(int) (Math.random() * RESPONSES_TO_CORRECT.length)];
        } else {
            return RESPONSES_TO_INCORRECT[(int) (Math.random() * RESPONSES_TO_INCORRECT.length)];
        }
    }

    public static class Question {
        private String text;
        private Emotion targetEmotion;
        private String[] targetThemes;
        private int minMatchingThemes;

        public Question(String text, int minMatchingThemes, String... targetThemes) {
            this.text = text;
            this.minMatchingThemes = minMatchingThemes;
            this.targetThemes = targetThemes;
        }

        public Emotion getTargetEmotion() {
            return targetEmotion;
        }

        public String[] getTargetThemes() {
            return targetThemes;
        }

        public int getMinMatchingThemes() {
            return minMatchingThemes;
        }

        public enum Emotion {
            CLEARLY_POSITIVE("positive+"),
            POSITIVE("positive"),
            MILDLY_POSITIVE("positive-"),
            NEUTRAL("neutral"),
            MILDLY_NEGATIVE("negative-"),
            NEGATIVE("negative"),
            CLEARLY_NEGATIVE("negative+");

            private String representation;

            Emotion(String representation) {
                this.representation = representation;
            }

            public static Emotion getEmotion(String representation) {
                for (Emotion e : Emotion.values()) {
                    if (e.representation.equals(representation))
                        return e;
                }
                return null;
            }
        }
    }

    public interface Marker {
        boolean markAnswer(Question question, String answer);
    }
}
