package uottahack2020.autism.model;

import android.widget.ProgressBar;

public interface Quest {

    void init();

    ActionPoint[] getActionPoints();

    <T extends Roadblock> ActionPoint findActionPoint(Class<T> type);

    void advance();

    void setProgressBar(ProgressBar progressBar);
}
