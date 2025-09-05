import java.util.ArrayList;


public class Addition extends ComplexExpression{
    public Addition(ArrayList<ComplexNumber> args){
        super(Operation.ADDITION,args);
    }
    @Override
    public ComplexNumber executeOneOperation(ComplexNumber n, ComplexNumber m){;
        return n.addition(m);
    }
}
