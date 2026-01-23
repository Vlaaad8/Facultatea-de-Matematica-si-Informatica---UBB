import com.example.Calculator;
import com.example.OperationException;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    public void testAdd() {
        assertEquals(15,calculator.run("+",2,3,4,5,1));
        assertEquals(10.5,calculator.run("+",2.4,3.6,4.5));
        assertThrows(RuntimeException.class, () -> calculator.run("+",2));
        assertEquals(40,calculator.run("+",10,30));
    }

    @Test
    public void testSubtract() {
        assertEquals(3,calculator.run("-",10,5,2));
        assertEquals(1.5,calculator.run("-",10,8.5));
        assertThrows(OperationException.class, () -> calculator.run("-",2));
    }

    @Test
    public void testMultiply() {
        assertEquals(120,calculator.run("*",2,3,4,5));
        assertEquals(86.625,calculator.run("*",4.5,3.5,5.5));
        assertThrows(OperationException.class, () -> calculator.run("*",2));

    }

    @Test
    public void testDivide() {
        assertEquals(5,calculator.run("/",10,2));
        assertThrows(OperationException.class, () -> calculator.run("/",2));
        assertThrows(ArithmeticException.class, () -> calculator.run("/",2,0));
        assertEquals(2.5,calculator.run("/",10,4));
    }

    @Test
    public void testMin(){
        assertEquals(1,calculator.run("min",1,2,3,4,5));
        assertThrows(OperationException.class,()-> calculator.run("min",1));
        assertEquals(1.3,calculator.run("min",1.31,1.30,3.23,44,5));
    }
    @Test
    public void testMax(){
        assertEquals(5,calculator.run("max",1,2,3,4,5));
        assertThrows(OperationException.class,()-> calculator.run("max",1));
        assertEquals(44.5,calculator.run("max",1.30,1.31,3.23,44.5,5));
    }
    @Test
    public void testSqrt(){
        assertEquals(5,calculator.run("sqrt",25));
        assertThrows(OperationException.class,()-> calculator.run("sqrt",25,43));
        assertEquals(1.7320508075688772,calculator.run("sqrt",3));
    }
}
