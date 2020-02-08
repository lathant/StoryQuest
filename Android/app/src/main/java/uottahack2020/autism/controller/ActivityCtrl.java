package uottahack2020.autism.controller;

import android.app.Activity;

public interface ActivityCtrl {

    void init(Activity activity);

    void updateInfo();

    default void onDestroy() {
    }
}
