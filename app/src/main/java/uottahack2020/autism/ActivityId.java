package uottahack2020.autism;

import android.content.Intent;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import uottahack2020.autism.fragment.Fragment;

public final class ActivityId {
    private static final HashMap<String, ActivityId> activityIds = new HashMap<>();

    private Class<? extends AppCompatActivity> activityClass;
    private String name;
    private int layoutId;

    public static ActivityId GET(String activityName) {
        ActivityId activityId = activityIds.get(activityName);
        if (activityId == null) {
            if (activityName.equals(MainActivity.TAG)) {
                activityId = new ActivityId(MainActivity.class, activityName, MainActivity.LAYOUT_ID);
            } else {
                throw new NullPointerException("Activity does not exist and cannot be started (because it is not known)");
            }
        }
        return activityId;
    }

    static ActivityId SET(Class<? extends AppCompatActivity> activityClass, String name, int layoutId) {
        ActivityId activityId;
        if (activityIds.containsKey(name)) {
            activityId = activityIds.get(name);
            activityId.layoutId = layoutId;
        } else {
            activityId = new ActivityId(activityClass, name, layoutId);
            activityIds.put(name, activityId);
        }
        return activityId;
    }

    private ActivityId(Class<? extends AppCompatActivity> activityClass, String name, int layoutId) {
        this.activityClass = activityClass;
        this.name = name;
        this.layoutId = layoutId;
    }

    public AppCompatActivity newInstance(AppCompatActivity sourceActivity, Fragment fragment) throws InstantiationException, IllegalAccessException {
        Intent intent = new Intent(sourceActivity, activityClass);
        fragment.startActivity(intent);
        return activityClass.newInstance();
    }

    String getName() {
        return name;
    }

    int getLayoutId() {
        return layoutId;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
