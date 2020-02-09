package uottahack2020.autism.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uottahack2020.autism.ActivityId;
import uottahack2020.autism.MainActivity;
import uottahack2020.autism.R;
import uottahack2020.autism.Session;
import uottahack2020.autism.controller.ConversationCtrl;
import uottahack2020.autism.controller.RecyclerAdapter;
import uottahack2020.autism.model.Conversation;

public class ConversationFragment extends Fragment<ConversationCtrl> {
    public static final String TAG = "ConversationFragment";
    private static final int LAYOUT_ID = R.layout.fragment_conversation;
    private static final String ACTIVITY_TAG = MainActivity.TAG;

    public static FragmentId setupId(ActivityId activityId) {
        return FragmentId.SET(ConversationFragment.class, TAG, LAYOUT_ID, activityId, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (controller == null) {
            controller = new ConversationCtrl(FragmentActivity.getSavedInstance(getFragmentId().getDefaultActivityId(), this));
        }
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(getFragmentId().getLayoutId(), container, false);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller.setConversation((Conversation) Session.CURRENT_QUEST.currentActionPoint().getRoadBlock());
        controller.init(view);

        RecyclerView chatsRecycler = view.findViewById(R.id.conversation_recyclerView);
        chatsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        chatsRecycler.setItemAnimator(new DefaultItemAnimator());

        RecyclerAdapter adapter = new RecyclerAdapter(getContext(), controller.observableChatHistory,
                R.layout.content_conversation_botbubble, R.anim.trans_fade_in);

        chatsRecycler.setAdapter(adapter);
        controller.setCardsAdapter(adapter);

        controller.updateInfo();
    }

    @Override
    public FragmentId getFragmentId() {
        return setupId(ActivityId.GET(ACTIVITY_TAG));
    }

    @Override
    public boolean onHomeUpPressed() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

