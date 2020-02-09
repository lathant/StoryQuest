package uottahack2020.autism.controller;

import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import uottahack2020.autism.R;
import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.model.Conversation;

public class ConversationCtrl implements FragmentCtrl {
    private FragmentActivity activity;
    private Conversation conversation;
    private RecyclerAdapter<ConversationBubble> cardsAdapter;

    private EditText editMessage;

    public List<ConversationBubble> observableChatHistory;

    public ConversationCtrl(FragmentActivity activity) {
        this.activity = activity;
        observableChatHistory = new ArrayList<>();
    }

    @Override
    public void init(View view) {
        editMessage = view.findViewById(R.id.conversation_editMessage);
        view.findViewById(R.id.conversation_btnSend).setOnClickListener(v -> {
            editMessage.setEnabled(false);
            conversation.markAnswer(editMessage.getText().toString());
        });

        conversation.setMarker(new Marker());
    }

    @Override
    public void updateInfo() {
        if (conversation.pushCurrentQuestion()) {
            editMessage.setEnabled(true);
        }
        observableChatHistory.clear();
        for (Conversation.ChatItem chatItem : conversation.getChatHistory()) {
            observableChatHistory.add(new ConversationBubble(
                    chatItem.isHuman() ? ConversationBubble.CardType.HUMAN : ConversationBubble.CardType.BOT,
                    chatItem,
                    activity));
        }
        cardsAdapter.notifyDataSetChanged();
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void setCardsAdapter(RecyclerAdapter<ConversationBubble> cardsAdapter) {
        cardsAdapter.setCardViewSelector(new RecyclerAdapter.CardViewSelector<ConversationBubble>() {
            @Override
            public int getItemViewType(ConversationBubble card) {
                switch (card.getType()) {
                    case BOT:
                        return 0;
                    case HUMAN:
                        return 1;
                    default:
                        return -1;
                }
            }

            @Override
            public int getViewLayoutId(int itemViewType) {
                switch (itemViewType) {
                    case 0:
                        return R.layout.content_conversation_botbubble;
                    case 1:
                        return R.layout.content_conversation_humanbubble;
                    default:
                        return -1;
                }
            }
        });
        this.cardsAdapter = cardsAdapter;
    }

    private class Marker implements Conversation.Marker {
        private RequestQueue queue;

        private Marker() {
            queue = Volley.newRequestQueue(activity);
        }

        public synchronized boolean markAnswer(Conversation.Question question, String answer) {
            Task task = new Task(answer);
            Thread taskThread = new Thread(task);
            taskThread.start();

            while (!task.complete) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }

            String[] analysis = task.response.split(Pattern.quote("|"));

            Conversation.Question.Emotion emotion = Conversation.Question.Emotion.getEmotion(analysis[0]);
            List<String> themes = new ArrayList<>();
            themes.addAll(Arrays.asList(analysis[1].split(",")));
            themes.addAll(Arrays.asList(analysis[2].split(",")));

            // if not passed emotion requirement
            if (!(question.getTargetEmotions().length == 0 || Arrays.asList(question.getTargetEmotions()).contains(emotion))) {
                return false;
            }

            List<String> targetThemes = new ArrayList<>(Arrays.asList(question.getTargetThemes()));

            int matchedThemes = 0;
            for (int i = targetThemes.size() - 1; i >= 0; --i) {
                if (themes.contains(targetThemes.get(i))) {
                    matchedThemes++;
                    targetThemes.remove(i);
                    if (matchedThemes >= question.getMinMatchingThemes())
                        return true;
                }
            }

            return targetThemes.isEmpty();
        }

        private class Task implements Runnable {
            private String answer;
            private String response;
            private boolean complete;

            private Task(String answer) {
                this.answer = answer;
            }

            @Override
            public void run() {
                String uri;
                try {
                    //http://storyquest.space/
                    uri = "https://uottahack-autism.appspot.com/?text=" + URLEncoder.encode(answer, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    complete = true;
                    Marker.this.notifyAll();
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                        response -> {
                            this.response = response;
                            complete = true;
                            Marker.this.notifyAll();
                        }, error -> {
                    complete = true;
                    Marker.this.notifyAll();
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
    }
}
