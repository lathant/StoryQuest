package uottahack2020.autism.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import uottahack2020.autism.controller.FragmentCtrl;

//import com.uniques.ourhouse.session.Session;
//import com.uniques.ourhouse.util.Model;

public abstract class Fragment<T extends FragmentCtrl> extends androidx.fragment.app.Fragment {
    //TODO fix saved objects
//    private static HashMap<String, Model> savedObjects = new HashMap<>();
    protected T controller;
    private boolean allowDestruction, pendingDestruction;
    protected boolean destroyOnCreateView;

//    protected static void saveObject(FragmentId fragmentId, String key, Model obj) {
//        savedObjects.put(fragmentId + ":" + key, obj);
//    }
//
//    protected static void saveDummy(FragmentId fragmentId, String key, Object obj) {
//        savedObjects.put(fragmentId + ":" + key, new DummyModel(obj));
//    }
//
//    protected static Model getSavedObject(FragmentId fragmentId, String key) {
//        return savedObjects.get(fragmentId + ":" + key);
//    }

//    @SuppressWarnings("unchecked")
//    protected static <T> T getDummy(FragmentId fragmentId, String key) {
////        DummyModel dummyModel = (DummyModel) savedObjects.get(fragmentId + ":" + key);
////        return (T) (dummyModel != null ? dummyModel.getObj() : null);
//    }

//    private static class DummyModel implements Model {
//        @NonNull
//        private Object obj;
//
//        private DummyModel(@NonNull Object obj) {
//            this.obj = obj;
//        }
//
//        @NonNull
//        Object getObj() {
//            return obj;
//        }
//
//        @Override
//        public String consoleFormat(String prefix) {
//            return obj.toString();
//        }
//
////        @Override
////        public JSONElement toJSON() {
////            return null;
////        }
////
////        @Override
////        public Object fromJSON(JSONElement json) {
////            return null;
////        }
//    }

    /*protected static void removeSavedObject(FragmentId fragmentId, String key) {
        savedObjects.remove(fragmentId + ":" + key);
    }*/

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        if (this.controller != null)
            this.controller.onDestroy();
        this.controller = controller;
    }

    private void clearSavedObjects(FragmentId fragmentId) {
//        List<String> keys = new ArrayList<>(savedObjects.keySet());
//        for (int i = 0; i < savedObjects.size(); i++) {
//            if (keys.get(i).startsWith(String.valueOf(fragmentId))) {
//                savedObjects.remove(keys.get(i));
//                keys.remove(i);
//                i--;
//            }
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (controller == null) destroyOnCreateView = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (destroyOnCreateView) {
            //TODO sort this out
//            allowDestruction();
//            destroy();
//            onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clearSavedObjects(getFragmentId());
        disallowDestruction();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveChanges();
    }

    private void saveChanges() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
    }

    protected void disallowDestruction() {
        allowDestruction = pendingDestruction = false;
    }

    protected void allowDestruction() {
        allowDestruction = true;
        if (pendingDestruction)
            destroy();
    }

    public void destroy() {
        if (!allowDestruction) {
            pendingDestruction = true;
        } else {
            clearSavedObjects(getFragmentId());
            if (controller != null) {
                controller.onDestroy();
                controller = null;
            }
            pendingDestruction = false;
        }
    }

    public abstract FragmentId getFragmentId();

    /**
     * @return true if this fragment overrides default behaviour, false otherwise
     */
    public abstract boolean onHomeUpPressed();

    /**
     * @return true if this fragment overrides default behaviour, false otherwise
     */
    public abstract boolean onBackPressed();
}
