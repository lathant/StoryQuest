package uottahack2020.autism.model;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import uottahack2020.autism.ActivityId;
import uottahack2020.autism.fragment.ConversationFragment;
import uottahack2020.autism.fragment.FragmentId;
import uottahack2020.autism.util.Observable;

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
    private Stack<ChatItem> chatHistory;

    Conversation(Quest quest, String situationText) {
        super(quest, situationText);
        currentQuestion = 0;
        chatHistory = new Stack<>();
    }

    public void setQuestions(Question... questions) {
        if (questions.length == 0) {
            throw new RuntimeException("You must provide at least 1 question");
        }
        this.questions = questions;
    }

    @Override
    public FragmentId getFragmentId(ActivityId activityId) {
        return ConversationFragment.setupId(activityId);
    }

    public Question getCurrentQuestion() {
        return questions[currentQuestion];
    }

    public boolean pushCurrentQuestion() {
        ChatItem cur = new ChatItem(getCurrentQuestion().getText(), false);
        if (chatHistory.search(cur) ==  -1) {
            chatHistory.push(cur);
            return true;
        }
        return false;
    }

    public Stack<ChatItem> getChatHistory() {
        return chatHistory;
    }

    public void setMarker(Marker marker) {
    }

    public void notifyAnswer(String answer, boolean isCorrect) {
        chatHistory.push(new ChatItem(answer, true));

        String response;
        if (isCorrect) {
            if (++currentQuestion >= questions.length) {
                forceComplete();
            }
            response = RESPONSES_TO_CORRECT[(int) (Math.random() * RESPONSES_TO_CORRECT.length)];
        } else {
            response = RESPONSES_TO_INCORRECT[(int) (Math.random() * RESPONSES_TO_INCORRECT.length)];
        }

        chatHistory.push(new ChatItem(response, false));
    }

    public static class Question {
        private Conversation conversation;
        private String text;
        private String prompt;
        @NonNull
        private Emotion[] targetEmotions;
        @NonNull
        private String[] targetThemes;
        private int minMatchingThemes;

        public Question(Conversation conversation, String text, String prompt, @NonNull Emotion[] targetEmotions) {
            this.conversation = conversation;
            this.text = text;
            this.prompt = prompt;
            this.targetEmotions = targetEmotions;
            this.minMatchingThemes = 0;
            this.targetThemes = new String[0];
        }

        public Question(String text, String prompt, @NonNull Emotion[] targetEmotions, int minMatchingThemes, @NonNull String[] targetThemes) {
            this.text = text;
            this.prompt = prompt;
            this.targetEmotions = targetEmotions;
            this.minMatchingThemes = minMatchingThemes;
            this.targetThemes = targetThemes;
        }

        public String getText() {
            return text;
        }

        public String getPrompt() {
            return prompt;
        }

        public Emotion[] getTargetEmotions() {
            return targetEmotions;
        }

        public String[] getTargetThemes() {
            return targetThemes;
        }

        public int getMinMatchingThemes() {
            return minMatchingThemes;
        }

        public Conversation getConversation() {
            return conversation;
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
        void markAnswer(Question question, String answer);
    }

    public class ChatItem implements Observable {
        private String text;
        private boolean isHuman;

        private ChatItem(String text, boolean isHuman) {
            this.text = text;
            this.isHuman = isHuman;
        }

        @Override
        public String getText() {
            return text;
        }

        public boolean isHuman() {
            return isHuman;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof ChatItem) {
                ChatItem item = (ChatItem) obj;
                return text.equals(item.text) && isHuman == item.isHuman;
            } else {
                return super.equals(obj);
            }
        }
    }
}
