package uottahack2020.autism.controller;

public interface ModelCtrl extends FragmentCtrl {

    /**
     * Duplication is not guaranteed.
     * You should check that the controller actually supports this.
     */
    void duplicateModel();

    /**
     * Deletion is not guaranteed.
     * You should check that the controller actually supports this.
     */
    void deleteModel();

//    boolean postModel(DatabaseLink database);
}
