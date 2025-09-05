import java.util.stream.Stream;

public class R12 {
    public static void main(String[] args) {
        Stream<String> ss = Stream.of("aop ","asd ","bus ");
        var res = ss
                .filter(s -> {
                    System.out.print(s);
                    return s.contains("a");
                })
                .map((x) ->
                {
                    System.out.print(x);
                    return x.toUpperCase();
                })
                .reduce("", (x, y) -> x + y);
        System.out.println(res);
    }}
