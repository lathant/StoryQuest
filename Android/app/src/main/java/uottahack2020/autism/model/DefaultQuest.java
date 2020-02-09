package uottahack2020.autism.model;

import android.widget.ProgressBar;

import uottahack2020.autism.ActivityId;
import uottahack2020.autism.fragment.FragmentId;

public class DefaultQuest implements Quest {
    private final int challengesToComplete = 3;

    private ActionPoint[] actionPoints;
    private int currentActionPoint;
    private int challengesCompleted;

    private ProgressBar progressBar;

    public DefaultQuest() {
        actionPoints = new ActionPoint[0];
        currentActionPoint = -1;
        challengesCompleted = 0;
    }

    public void init() {
        Conversation conversation1 = new Conversation(
                this,
                "Someone at school has lost their toy. Put your empathy skill to the test!") {
            @Override
            public String getName() {
                return "The Lost Toy";
            }

            @Override
            public String getDescription() {
                return "Sometimes others need a little support";
            }
        };

        conversation1.setQuestions(new Conversation.Question(
                conversation1,
                "Hi, I lost my toy at recess today.",
                "How can you respond?",
                new Conversation.Question.Emotion[]{
                        Conversation.Question.Emotion.CLEARLY_POSITIVE,
                        Conversation.Question.Emotion.MILDLY_POSITIVE,
                        Conversation.Question.Emotion.POSITIVE,
                        Conversation.Question.Emotion.NEUTRAL
                }));

        Story story1 = new Story(
                this,
                "Billy practiced for his race for a month. " +
                        "Today, he won an award for 1st place. How does Billy feel?",
                Emoji.BOY,
                new Story.Option(false, "Upset", "Hint: Think of the opposite of being upset"),
                new Story.Option(false, "Bored", "Not quite -- he was looking forward to this race all month!"),
                new Story.Option(true, "Happy", "Billy feels accomplished; he is happy that his hard work paid off"),
                new Story.Option(false, "Neutral", "His practice was not easy, so he was feeling positive after he won")) {

            @Override
            public FragmentId getFragmentId(ActivityId activityId) {
                return null; //todo
            }

            @Override
            public String getName() {
                return "Billy and the Race";
            }

            @Override
            public String getDescription() {
                return "description";
            }
        };

        SecondStory story2 = new SecondStory(
                this,
                "Tanya went to the store to buy bread. Somebody pushed her when she was in line, and she fell. " +
                        "The bread dropped on the floor and she started to cry. How does she feel?",
                Emoji.GIRL,
                new Story.Option(false, "Confused", "Not quite -- she started crying so she must be more than just confused"),
                new Story.Option(true, "Embarrassed", "Because she feel and dropped the break in-front of everyone"),
                new Story.Option(false, "Indifferent", "She lost what she came to buy from the bakery and this is not something that she expected to happen"),
                new Story.Option(false, "Joyful", "If she was crying, think of the opposite of being joyful")) {

            @Override
            public FragmentId getFragmentId(ActivityId activityId) {
                return null; //todo
            }

            @Override
            public String getName() {
                return "Dropped the bread";
            }

            @Override
            public String getDescription() {
                return "description";
            }
        };

        actionPoints = new ActionPoint[]{
                new ActionPoint() {
                    @Override
                    public Roadblock getRoadBlock() {
                        return story1;
                    }

                    @Override
                    public boolean isCompleted() {
                        return story1.isComplete();
                    }
                },
                new ActionPoint() {
                    @Override
                    public Roadblock getRoadBlock() {
                        return conversation1;
                    }

                    @Override
                    public boolean isCompleted() {
                        return conversation1.isComplete();
                    }
                },
                new ActionPoint() {
                    @Override
                    public Roadblock getRoadBlock() {
                        return story2;
                    }

                    @Override
                    public boolean isCompleted() {
                        return story2.isComplete();
                    }
                }
        };
        currentActionPoint = 0;
    }

    @Override
    public ActionPoint[] getActionPoints() {
        return actionPoints;
    }

    @Override
    public <T extends Roadblock> ActionPoint findActionPoint(Class<T> type) {
        for (ActionPoint actionPoint : actionPoints) {
            try {
                actionPoint.getRoadBlock().getClass().asSubclass(type);
                if (actionPoint.isCompleted()) {
                    init();
                    return findActionPoint(type);
                } else {
                    return actionPoint;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public void advance() {
        currentActionPoint++;
        challengesCompleted = Math.min(challengesCompleted + 1, challengesToComplete);
        System.out.println("COMPLETEION PERCENT " + challengesCompleted + " " + getCompletionPercent());
        progressBar.setProgress(Math.max(getCompletionPercent(), 10));
    }

    @Override
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public int getCompletionPercent() {
        return (challengesCompleted * 100) / challengesToComplete;
    }

    public abstract class SecondStory extends Story {

        protected SecondStory(Quest quest, String situationText, Emoji avatar, Option... options) {
            super(quest, situationText, avatar, options);
        }
    }
}
