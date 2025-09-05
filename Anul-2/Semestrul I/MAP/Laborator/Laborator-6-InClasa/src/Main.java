import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) {
        ArrayList<String> cuvinte= new ArrayList<String>();
        cuvinte.add("A");
        cuvinte.add("salut");
        cuvinte.add("ce");
        cuvinte.add("faci");
        cuvinte.add("a");
        Stream<String> stream=cuvinte.stream();
        ArrayList<String>newList=stream.map(x->x.toUpperCase()).collect(Collectors.toCollection(ArrayList::new));
        System.out.println(newList);

        ArrayList<Dog> dogs=new ArrayList<>();
        Dog d1=new Dog("Ana",12);
        Dog d2=new Dog("Bogdan",12);
        Dog d3=new Dog("Lavinia",2);
        dogs.add(d1);
        dogs.add(d2);
        dogs.add(d3);
        List<Dog> c=dogs.stream().filter(x->x.getAge()>10).collect(Collectors.toList());
        c.forEach(n-> System.out.println(n.toString()));
        List<Dog> c2=dogs.stream().sorted(Comparator.comparing(Dog::getAge)).collect(Collectors.toList());
        c2.forEach(n-> System.out.println(n.toString()));
        List<Dog> c3=dogs.stream().filter(x->x.getName().equals("Ana")).collect(Collectors.toList());
        c3.forEach(n-> System.out.println(n.toString()));
        dogs.stream().forEach(x-> System.out.println(x.getName()));

        ArrayList<Integer> numere=new ArrayList<>();
        numere.add(1);
        numere.add(2);
        numere.add(3);
        numere.add(4);
        ArrayList<Integer> patrate=numere.stream().map(x->x*x).collect(Collectors.toCollection(ArrayList::new));
        System.out.println(patrate);
        ArrayList<Mail> mails=new ArrayList<>();
        Mail m1=new Mail("A","b",1940,20.3);
        Mail m2=new Mail("B","c",1940,22.3);
        Mail m3=new Mail("L","d",1940,22.4);
        Mail m4=new Mail("L","e",1940,22.5);
        mails.add(m1);
        mails.add(m2);
        mails.add(m3);
        mails.add(m4);
        Optional<Double> m=mails.stream().map(x->x.getProfit()*x.getProfit()).max(Comparator.naturalOrder());
        System.out.println("Max Doubled Profit: " + m.orElse(null));

    }
}