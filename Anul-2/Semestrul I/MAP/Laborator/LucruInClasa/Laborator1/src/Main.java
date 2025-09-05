import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (String element : args) {
            arrayList.add(element);
        }
        ExpressionParser parser = new ExpressionParser(arrayList);
        ComplexExpression expr = parser.parser();
        ComplexNumber result = expr.execute();
        System.out.println("Rezultatul expresiei este: " + result.toString());
    }
}
