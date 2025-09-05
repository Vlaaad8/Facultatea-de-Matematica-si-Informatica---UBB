
import java.util.HashMap;
import java.util.Map;
class MapMergeExample {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 10);
        map.put("B", 20);
        map.merge("A", 5, Integer::sum);
        map.merge("C", 15, Integer::sum);
        System.out.println(map);
    }
}
