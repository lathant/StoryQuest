package uottahack2020.autism.controller;

import android.content.Context;
import android.util.Log;

import uottahack2020.autism.model.Conversation;

class ConversationMarker implements Conversation.Marker {
    private ConversationCtrl conversationCtrl;
    private Context context;

    private Object[] result;

    public ConversationMarker(ConversationCtrl conversationCtrl, Context context) {
        this.conversationCtrl = conversationCtrl;
        this.context = context;
    }

    public void markAnswer(Conversation.Question question, String answer) {
        MarkerTask task = new MarkerTask(context, question.getConversation(), this, answer);
//        task.execute();
        task.doInBackground();
    }

    public synchronized Object[] getResult() {
        while (result == null) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        Log.d("convo", "out of get wait");
        Object[] result = this.result;
        this.result = null;
        notifyAll();
        return result;
    }

    public synchronized void postResult(String answer, boolean result) {
        while (this.result != null) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        this.result = new Object[] {answer, result};
        notifyAll();
    }

}
