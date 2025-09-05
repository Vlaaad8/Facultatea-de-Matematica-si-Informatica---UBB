import java.util.ArrayList;


public class Division extends ComplexExpression{
    public Division(ArrayList<ComplexNumber> args){
        super(Operation.DIVISION,args);
    }
    @Override
    public ComplexNumber executeOneOperation(ComplexNumber n, ComplexNumber m){;
        return n.division(m);
    }
}
