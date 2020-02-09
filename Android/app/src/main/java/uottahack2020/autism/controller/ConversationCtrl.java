package uottahack2020.autism.controller;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import uottahack2020.autism.fragment.ConversationFragment;
import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.fragment.FragmentId;
import uottahack2020.autism.model.Conversation;

public class ConversationCtrl implements FragmentCtrl {
    private FragmentActivity activity;
    private Conversation conversation;
    private RecyclerAdapter<ConversationBubble> cardsAdapter;
    private ConversationMarker marker;
    private RequestQueue queue;

    private TextView txtSituation;
    private EditText editMessage;

    public List<ConversationBubble> observableChatHistory;

    public ConversationCtrl(FragmentActivity activity) {
        this.activity = activity;
        observableChatHistory = new ArrayList<>();
        marker = new ConversationMarker(this, activity);
        queue = Volley.newRequestQueue(activity);
    }

    @Override
    public void init(View view) {
        txtSituation = view.findViewById(R.id.conversation_txtSituation);
        editMessage = view.findViewById(R.id.conversation_editMessage);
        view.findViewById(R.id.conversation_btnSend).setOnClickListener(v -> {
            editMessage.setEnabled(false);

            String answer = editMessage.getText().toString();

            String uri;
            try {
                //http://storyquest.space/
                uri = "https://uottahack-autism.appspot.com/nla?text=" + URLEncoder.encode(answer, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                    response -> {
                        Conversation.Question question = conversation.getCurrentQuestion();
                        String[] analysis = response.split(Pattern.quote("|"));
                        System.out.println("ConversationCtrl: res= " + response);

                        Conversation.Question.Emotion emotion = Conversation.Question.Emotion.getEmotion(analysis[0]);
                        System.out.println("ConversationCtrl: emotion= " + emotion.toString());

                        List<String> themes = new ArrayList<>();
                        if (analysis.length == 2) {
                            themes.addAll(Arrays.asList(analysis[1].split(",")));
                        }
                        if (analysis.length == 3) {
                            themes.addAll(Arrays.asList(analysis[2].split(",")));
                        }

                        // if not passed emotion requirement
                        if (!(question.getTargetEmotions().length == 0 || Arrays.asList(question.getTargetEmotions()).contains(emotion))) {
                            conversation.notifyAnswer(answer, false);
                            updateInfo();
                            return;
                        }

                        if (question.getMinMatchingThemes() == 0) {
                            conversation.notifyAnswer(answer, true);
                            updateInfo();
                            return;
                        }

                        List<String> targetThemes = new ArrayList<>(Arrays.asList(question.getTargetThemes()));

                        int matchedThemes = 0;
                        for (int i = targetThemes.size() - 1; i >= 0; --i) {
                            boolean found = false;
                            String targetTheme = targetThemes.get(i);
                            for (String theme : themes) {
                                if (theme.contains(targetTheme)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                matchedThemes++;
                                targetThemes.remove(i);
                                if (matchedThemes >= question.getMinMatchingThemes()) {
                                    conversation.notifyAnswer(answer, true);
                                    updateInfo();
                                    return;
                                }
                            }
                        }

                        conversation.notifyAnswer(answer, targetThemes.isEmpty());
                        updateInfo();
                    }, error -> {
                conversation.notifyAnswer(answer, false);
                updateInfo();
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

            editMessage.setEnabled(true);
        });
        view.findViewById(R.id.conversation_btnExit).setOnClickListener(v -> {
            activity.popFragment(FragmentId.GET(ConversationFragment.TAG));
            activity.overridePendingTransition(R.anim.trans_top_in, R.anim.trans_bottom_out);
        });
        conversation.setMarker(marker);
    }

    @Override
    public void updateInfo() {
        if (!conversation.isComplete()) {
            txtSituation.setText(conversation.getCurrentQuestion().getText());
        } else {
            activity.popFragment(FragmentId.GET(ConversationFragment.TAG));
            activity.overridePendingTransition(R.anim.trans_top_in, R.anim.trans_bottom_out);
            return;
        }

        conversation.pushCurrentQuestion();
        editMessage.setEnabled(true);

        editMessage.setText("");

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
                System.out.println("ConversationCtrl: card= " + card.getType().toString());
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

}
