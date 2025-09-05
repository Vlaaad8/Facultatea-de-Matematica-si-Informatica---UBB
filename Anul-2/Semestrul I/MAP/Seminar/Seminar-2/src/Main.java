import com.sun.source.util.Trees;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Student s1 = new Student("Dan", 4.5f);
        Student s2 = new Student("Ana", 8.5f);
        Student s3 = new Student("Dan", 4.5f);
        Student s4 = new Student("Danut", 7.5f);
        Set<Student> studSet = new HashSet<>();
        studSet.add(s1);
        studSet.add(s2);
        studSet.add(s3);
//        for (Student stud : studSet) {
//            System.out.println(stud);
//        }
        TreeSet<Student> treeSet = new TreeSet<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o2.compareTo(o1);
            }
        });
        treeSet.add(s1);
        treeSet.add(s2);
        treeSet.add(s3);
        treeSet.add(s4);
        for (Student stud : treeSet) {
            System.out.println(stud);
        }
        MyMap myMap = new MyMap();
        for (Student stud : getList()) {
            myMap.add(stud);
        }
        for(var entry :myMap.getEntries()){
            System.out.println("Studentii cu media:"+ entry.getKey()+ "sunt:");
            //System.out.println(entry.getValue());
            TreeSet<Student>sorted=new TreeSet<>();
            for(Student stud:entry.getValue()){
                sorted.add(stud);
            }
        }
        Repository<Long,Student>repo=new InMemoryRepository<>();
        long id=0;
        for (var s: getList()){
            s.setId(id);
            repo.save(s);
            id++;
        }
        System.out.println("ok");
    }
    public static List<Student> getList(){
        List<Student> l=new ArrayList<Student>();
        l.add(new Student("1",9.7f));
        l.add(new Student("2",7.3f));
        l.add(new Student("3",6f));
        l.add(new Student("4",6.9f));
        l.add(new Student("5",9.5f));
        l.add(new Student("6",9.9f));
        return l;
    }

}