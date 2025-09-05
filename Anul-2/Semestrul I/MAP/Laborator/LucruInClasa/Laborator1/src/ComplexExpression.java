import java.util.ArrayList;
import java.util.List;

public abstract class ComplexExpression {
    Operation operation;
    ArrayList<ComplexNumber> args;

    public ComplexExpression(Operation op, ArrayList<ComplexNumber> list) {
        this.operation = op;
        this.args = list;
    }

    public abstract ComplexNumber executeOneOperation(ComplexNumber n, ComplexNumber m);

    public final ComplexNumber execute() {
        ComplexNumber result = args.getFirst();
        for (int i = 1; i < args.size(); i++) {
            result = executeOneOperation(result, args.get(i));
        }
        return result;
    }
}
