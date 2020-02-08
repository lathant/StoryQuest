package uottahack2020.autism.controller;

import android.view.View;

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

import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.model.Conversation;

public class ConversationCtrl implements FragmentCtrl {
    private FragmentActivity activity;
    private Conversation conversation;

    public ConversationCtrl(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void init(View view) {

    }

    @Override
    public void updateInfo() {

    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    private class Marker implements Conversation.Marker {
        private RequestQueue queue;

        private Marker() {
            queue = Volley.newRequestQueue(activity);
        }

        public boolean markAnswer(Conversation.Question question, String answer) {
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
            if (!(question.getTargetEmotion() == null || emotion == question.getTargetEmotion())) {
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
                    uri = "http://storyquest.space/?text=" + URLEncoder.encode(answer, "UTF-8");
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
