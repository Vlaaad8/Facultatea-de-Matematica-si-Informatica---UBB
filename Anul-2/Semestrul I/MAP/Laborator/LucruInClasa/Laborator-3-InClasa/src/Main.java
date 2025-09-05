import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Circle c1 = new Circle(2.4);
        Circle c2 = new Circle(2.3);
        Circle c3 = new Circle(2.2);
        ArrayList<Circle> listCircle = new ArrayList<Circle>();
        listCircle.add(c1);
        listCircle.add(c2);
        listCircle.add(c3);
        Circle lastCircle = EelementUtils.lastEntry(listCircle);
        System.out.println(lastCircle);


        ArrayList<Integer> listInteger = new ArrayList<Integer>();
        listInteger.add(1030);
        listInteger.add(2033);
        Integer lastInteger = EelementUtils.lastEntry(listInteger);
        System.out.println(lastInteger);
        //Part 2

        ArrayList<Circle> randomCircles=new ArrayList<Circle>();
        double randomRadius= Math.random();
        while(randomRadius>0.01){
            randomCircles.add(new Circle(randomRadius));
            randomRadius=Math.random();
        }
        for(Circle circle:randomCircles){
            double aria=circle.getRadius()*circle.getRadius()*Math.PI;
            System.out.println("Aria cercului este:"+ aria);
        }
        Map<String,String> employeesMap= new HashMap<>();
        employeesMap.put("a1234","Balahura Vlad");
        employeesMap.put("a134","Pop Emilia");
        employeesMap.put("AA34234","Brumar Mihnea");
        employeesMap.put("B13444","Vranca Andrei");

        employeesMap.


    }
    }
