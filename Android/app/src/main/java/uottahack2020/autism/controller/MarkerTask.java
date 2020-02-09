package uottahack2020.autism.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import uottahack2020.autism.model.Conversation;

class MarkerTask extends AsyncTask<Void, Void, Void> {
    private Conversation conversation;
    private ConversationMarker marker;
    private String answer;

    MarkerTask(Context context, Conversation conversation, ConversationMarker marker, String answer) {
        this.conversation = conversation;
        this.marker = marker;
        this.answer = answer;
    }

    @Override
    protected Void doInBackground(Void... params) {

        return null;
    }
}
