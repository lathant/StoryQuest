package uottahack2020.autism.model;

import uottahack2020.autism.fragment.FragmentId;

public interface Roadblock {

    FragmentId getFragmentId();

    String getName();

    String getDescription();

    boolean isComplete();

}
