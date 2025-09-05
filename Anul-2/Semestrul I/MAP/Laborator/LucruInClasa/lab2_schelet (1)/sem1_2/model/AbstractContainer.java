package sem1_2.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer implements Container {
    protected ArrayList<Task> list;
    AbstractContainer(){
        this.list=new ArrayList<>();
    }
    @Override
    public int size(){
        return list.size();
    }
    @Override
    public boolean isEmpty(){
        if(list.isEmpty()){
            return true;
        }
        else
            return false;

    };
    @Override
    public void add(Task task) {
        list.add(task);
    }
}

