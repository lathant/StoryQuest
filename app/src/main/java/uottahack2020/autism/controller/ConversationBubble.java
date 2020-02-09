package uottahack2020.autism.controller;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import uottahack2020.autism.R;
import uottahack2020.autism.fragment.FragmentActivity;
import uottahack2020.autism.util.Observable;

public final class ConversationBubble implements RecyclerCard {

    public enum CardType {
        BOT, HUMAN
    }

    private CardType type;
    private FragmentActivity activity;
    private Observable object;
    private TextView txtMessage;

    public ConversationBubble(CardType type, Observable object, FragmentActivity activity) {
        this.type = type;
        this.object = object;
        this.activity = activity;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public void attachLayoutViews(View layout) {
        txtMessage = layout.findViewById(R.id.conversation_bubble_txt);

//        layout.setOnClickListener(v -> handleClick());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateInfo() {
        txtMessage.setText(object.getText());
    }

    private void handleClick() {
    }
}
