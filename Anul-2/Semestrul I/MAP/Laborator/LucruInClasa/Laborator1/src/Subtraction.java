import java.util.ArrayList;


public class Subtraction extends ComplexExpression{
    public Subtraction(ArrayList<ComplexNumber> args){
        super(Operation.SUBTRACTION,args);
    }
    @Override
    public ComplexNumber executeOneOperation(ComplexNumber n, ComplexNumber m){;
        return n.subtraction(m);
    }
}
