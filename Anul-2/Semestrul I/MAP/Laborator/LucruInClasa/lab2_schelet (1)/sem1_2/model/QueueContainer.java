package sem1_2.model;

import java.util.ArrayList;
import java.util.List;

public class QueueContainer extends AbstractContainer {

    public QueueContainer(){
        super();
    }
    @Override
    public Task remove() {
        if (list.isEmpty()) {
            return null;
        } else {
            return list.removeFirst();
        }
    }
}
