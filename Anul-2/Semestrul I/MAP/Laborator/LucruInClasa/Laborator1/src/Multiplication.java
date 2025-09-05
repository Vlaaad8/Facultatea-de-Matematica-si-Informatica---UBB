import java.util.ArrayList;


public class Multiplication extends ComplexExpression{
    public Multiplication(ArrayList<ComplexNumber> args){
        super(Operation.MULTIPLICATION,args);
    }
    @Override
    public ComplexNumber executeOneOperation(ComplexNumber n, ComplexNumber m){;
        return n.multiplication(m);
    }
}
