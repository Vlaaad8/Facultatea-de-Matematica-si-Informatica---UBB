package sem1_2.model;
import java.util.Arrays;
import java.util.List;

public class SortingTask extends Task{
    private final int[] toSort;
    public AbstractSorter sorter;

    public SortingTask(int[] toSort,String taskId, String description,AbstractSorter sorter) {
        super(taskId, description);
        this.toSort = toSort;
        this.sorter = sorter;
    }
    @Override
    public void execute() {
        sorter.sort(toSort);
        System.out.println(Arrays.toString(toSort));
    }
}

