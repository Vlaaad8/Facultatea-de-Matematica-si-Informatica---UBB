import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionParser {
    private ArrayList<String> arguments;

    public ExpressionParser(ArrayList<String> args) {
        this.arguments = args;

    }

    public ComplexExpression parser() {
        String operation= arguments.get(1);
        ArrayList<ComplexNumber> numbers=new ArrayList<>();
        for (int p = 0; p < arguments.size(); p++) {
            boolean negative = false;
            if (p % 2 == 0) {
                String[] element = arguments.get(p).split("[+]", 2);
                if (element.length != 2) {
                    negative = true;
                    element = arguments.get(p).split("-", 2);
                }
                double realPart = Double.parseDouble(element[0]);
                String[] elementImaginary = element[1].split("[*]", 2);
                double imaginaryPart = Double.parseDouble(elementImaginary[0]);
                if (negative) {
                    imaginaryPart = -imaginaryPart;
                }
                ComplexNumber number=new ComplexNumber(realPart,imaginaryPart);
                numbers.add(number);
            } else {
                operation = arguments.get(p);
                if (!Objects.equals(operation, "+") && !Objects.equals(operation, "-") && !Objects.equals(operation, "*") && !Objects.equals(operation, "/")) {
                    return null;
                }
            }
        }
        return switch (operation) {
            case "+" -> ExpressionFactory.getInstance().createExpression(Operation.ADDITION, numbers);
            case "-" -> ExpressionFactory.getInstance().createExpression(Operation.SUBTRACTION, numbers);
            case "*" -> ExpressionFactory.getInstance().createExpression(Operation.MULTIPLICATION, numbers);
            case "/" -> ExpressionFactory.getInstance().createExpression(Operation.DIVISION, numbers);
            default -> null;
        };

    }

}
