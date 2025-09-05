import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ArieHelper {
    public static void main(String[] args) {
    Function<String,Integer> convertMethodReference=Integer::valueOf;

        List<Patrat> patrate = List.of(new Patrat(2), new Patrat(3), new Patrat(4), new Patrat(5));
        List<Cerc> cercuri = List.of(new Cerc(2), new Cerc(3), new Cerc(4));
//        Aria<Patrat> ariePatrat=  x->{return x.getLat()*x.getLat();};
////        Aria<Cerc> arieCerc=  x->{return x.getRaza()*x.getRaza()*Math.PI;};
//        Aria<Patrat> ariePatrat=  x->x.getLat()*x.getLat();
//        Aria<Cerc> arieCerc=  x->x.getRaza()*x.getRaza()*Math.PI;
//        patrate.forEach(x->System.out.println(ariePatrat.calculate(x)));
//        cercuri.forEach(x->System.out.println(arieCerc.calculate(x)));
        Aria<Patrat> ariaPatrat = ArieHelper::ariePatrat;
        Aria<Cerc> ariaCerc = ArieHelper::arieCerc;
        Predicate<String> lungimePara = s -> s.length() % 2 == 0;
        List<String> lista = List.of("mancare", "pui", "Orez", "prajitura", "para");
        afiseazaCriteriu(lista, lungimePara);

        Predicate<String> incepeP = s ->
                s.startsWith("p");
        afiseazaCriteriu(lista, incepeP);
        Function<String, Integer> converterLambda = x -> Integer.valueOf(x);
        System.out.println(converterLambda.apply("14"));
        Predicate<Character> eVocala=x-> {
            String vocale="AEIOUaeiou";
            return vocale.contains(x.toString());
        };
        Function<String, String> convertPasareasca = x -> {
            String rez = "";
            for(int i=0;i<x.length();i++){
                rez+=x.charAt(i);
                if(eVocala.test(x.charAt(i))){
                    rez+="p"+x.charAt(i);
                }
            }
            return rez;
        };
        System.out.println(convertPasareasca.apply("Mama merge la piata"));
        System.out.println(convertPasareasca.apply("Un vultur sta pe pisc cu un pix in plisc"));
        Supplier<Cerc> supplier =()->new Cerc();
        Cerc c1=supplier.get();
        Supplier<Cerc> supplier2=Cerc::new;
        Cerc c2=supplier2.get();
        System.out.println(c1);
        System.out.println(c2);

        //Ultimul exercitiu - cerinta de laborator
        //De adaugat inca o operatie
        List<String> list=List.of("asd","bce","asc","bcr","cc");
        Stream<String> stream=list.stream();
        stream.filter(x->{
                    System.out.println(x);
            return x.startsWith("b");
        })
                .map(x->{
                    System.out.println(x);
                    return x.toUpperCase();
                })
                .forEach(System.out::println);
        List<Integer> integerList=List.of(1,2,3,34,5,5,54);
        Stream<Integer> stream2=integerList.stream();
        Optional<Integer> rez=stream2.reduce((a, b)->a+b);
        System.out.println(rez.toString());
    }

    public static <E> void afiseazaCriteriu(List<E> l, Predicate<E> criteriu) {
        l.forEach(element -> {
            if (criteriu.test(element))
                System.out.println(element);
        });
    }

    public static double ariePatrat(Patrat patrat) {
        return patrat.getLat() * patrat.getLat();
    }

    public static double arieCerc(Cerc cerc) {
        return cerc.getRaza() * cerc.getRaza() * Math.PI;
    }
}

