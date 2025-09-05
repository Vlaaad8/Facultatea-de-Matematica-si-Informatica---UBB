import java.util.*;

public class MyMap {
    private TreeMap<Integer, List<Student>> studentsMap;
    //nu putem folosi tipuri primitive de date

    public MyMap() {
        studentsMap = new TreeMap<>(new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
    }
    public void add (Student student) {
        int medie=Math.round(student.getMedia());
        List<Student> returnat= studentsMap.get(medie);
        if (returnat == null) {
            returnat = new ArrayList<>();
            returnat.add(student);
            studentsMap.put(medie, returnat);

        }
        else{
            returnat.add(student);
        }
    }
    public Set<Map.Entry<Integer,List<Student>>> getEntries() {
        return studentsMap.entrySet();
    }
}


