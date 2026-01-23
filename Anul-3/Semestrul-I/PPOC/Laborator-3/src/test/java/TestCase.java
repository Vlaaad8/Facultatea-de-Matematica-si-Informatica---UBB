import com.example.BigDecimals;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCase {


    public BigDecimals bigDecimals;

    @Test
    void testSumNormal() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("10"),
                new BigDecimal("20"),
                new BigDecimal("30")
        );
        bigDecimals = new BigDecimals(list);
        assertEquals(new BigDecimal("60"),bigDecimals.sum());
    }

    @Test
    void testSumWithDifferentScale() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("10.0"),
                new BigDecimal("5.00")
        );
        bigDecimals = new BigDecimals(list);
        assertEquals(new BigDecimal("15.00"),bigDecimals.sum());
    }

    @Test
    void testSumNegativeValues() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("-10"),
                new BigDecimal("15"),
                new BigDecimal("-6")
        );
        bigDecimals = new BigDecimals(list);
        assertEquals(new BigDecimal("-1"), bigDecimals.sum());
    }

    @Test
    void testAverageNormal() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("10"),
                new BigDecimal("20"),
                new BigDecimal("30")
        );
        bigDecimals = new BigDecimals(list);
        assertEquals(new BigDecimal("20"), bigDecimals.average());
    }

    @Test
    void testAverageWithDifferentScale() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("1.5"),
                new BigDecimal("2.5"),
                new BigDecimal("3.50")
        );
        bigDecimals = new BigDecimals(list);
        assertEquals(new BigDecimal("2.50"), bigDecimals.average());
    }

    @Test
    void testAverageEmpty() {
        bigDecimals = new BigDecimals(List.of()); assertEquals(BigDecimal.ZERO, bigDecimals.average());
    }


    @Test
    void testTopGets10Percent() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("1"),
                new BigDecimal("2"),
                new BigDecimal("3"),
                new BigDecimal("4"),
                new BigDecimal("5"),
                new BigDecimal("6"),
                new BigDecimal("7"),
                new BigDecimal("8"),
                new BigDecimal("9"),
                new BigDecimal("10")
        );
        bigDecimals = new BigDecimals(list);
        List<BigDecimal> top = bigDecimals.top();

        assertEquals(1, top.size());
        assertEquals(new BigDecimal("10"), top.get(0));
    }

    @Test
    void testTopSmallList() {
        List<BigDecimal> list = Arrays.asList(new BigDecimal("4"), new BigDecimal("7"));
        bigDecimals = new BigDecimals(list);
        assertTrue(bigDecimals.top().isEmpty());
    }

    @Test
    void testTopWithDuplicates() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("5"),
                new BigDecimal("10"),
                new BigDecimal("10"),
                new BigDecimal("10"),
                new BigDecimal("3"),
                new BigDecimal("1"),
                new BigDecimal("8"),
                new BigDecimal("2"),
                new BigDecimal("7"),
                new BigDecimal("6")
        );
        bigDecimals = new BigDecimals(list);
        List<BigDecimal> top = bigDecimals.top();

        assertEquals(1, top.size());
        assertEquals(new BigDecimal("10"), top.get(0));
    }
}