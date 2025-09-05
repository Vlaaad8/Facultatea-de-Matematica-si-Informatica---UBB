import java.util.ArrayList;

public class EelementUtils<T> {
    private T value;
    public static <T> T lastEntry(ArrayList<T> list){
       return list.getLast();

    }
}
