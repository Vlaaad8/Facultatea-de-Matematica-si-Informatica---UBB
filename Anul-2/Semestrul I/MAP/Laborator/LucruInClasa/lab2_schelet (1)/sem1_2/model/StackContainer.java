package sem1_2.model;

import java.util.ArrayList;
import java.util.List;

public class StackContainer extends AbstractContainer {
    public StackContainer() {
        super();
    }

    public Task remove() {
        if (!super.isEmpty()){
            return list.remove(super.size() - 1);
        } else{
            return null;
        }
    }
}
