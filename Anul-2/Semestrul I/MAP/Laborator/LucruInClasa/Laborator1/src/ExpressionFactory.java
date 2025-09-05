import java.util.ArrayList;

public class ExpressionFactory {
    private ExpressionFactory(){

    };
    public static ExpressionFactory instance=new ExpressionFactory();

    public static ExpressionFactory getInstance(){
        if(instance!=null){
            return instance;
        }
        return null;
    }

    public ComplexExpression createExpression(Operation operation, ArrayList<ComplexNumber> args){
        return switch (operation) {
            case ADDITION -> new Addition(args);
            case MULTIPLICATION -> new Multiplication(args);
            case DIVISION -> new Division(args);
            case SUBTRACTION -> new Subtraction(args);
        };


    };

}
