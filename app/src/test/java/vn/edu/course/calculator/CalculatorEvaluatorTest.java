package vn.edu.course.calculator;

import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CalculatorEvaluatorTest {

    @Test
    public void testOperatorPrecedence() {
        CalculatorEvaluator.EvaluationResult res = CalculatorEvaluator.evaluate("2 + 3 × 4");
        assertTrue(res.isSuccess());
        assertEquals("14", res.getResult());

        CalculatorEvaluator.EvaluationResult res2 = CalculatorEvaluator.evaluate("10 − 4 ÷ 2");
        assertTrue(res2.isSuccess());
        assertEquals("8", res2.getResult());

        CalculatorEvaluator.EvaluationResult res3 = CalculatorEvaluator.evaluate("5 + 2 × 3 − 4 ÷ 2");
        assertTrue(res3.isSuccess());
        assertEquals("9", res3.getResult());
    }

    @Test
    public void testDivisionByZero() {
        CalculatorEvaluator.EvaluationResult res = CalculatorEvaluator.evaluate("5 ÷ 0");
        assertFalse(res.isSuccess());
        assertTrue(res.isDivideByZero());
    }

    @Test
    public void testDecimals() {
        CalculatorEvaluator.EvaluationResult res = CalculatorEvaluator.evaluate("2.5 + 3.5");
        assertTrue(res.isSuccess());
        assertEquals("6", res.getResult());

        CalculatorEvaluator.EvaluationResult res2 = CalculatorEvaluator.evaluate("1 ÷ 4");
        assertTrue(res2.isSuccess());
        assertEquals("0.25", res2.getResult());
    }

    @Test
    public void testIncompleteOrInvalidExpression() {
        CalculatorEvaluator.EvaluationResult res = CalculatorEvaluator.evaluate("2 + ");
        assertFalse(res.isSuccess());
        assertFalse(res.isDivideByZero());
    }

    @Test
    public void testHistoryEntryEqualityAndExportText() {
        Instant now = Instant.parse("2026-11-10T06:00:00Z");
        HistoryEntry a = new HistoryEntry(1L, "2 + 3 × 4", "14", now);
        HistoryEntry b = new HistoryEntry(1L, "2 + 3 × 4", "14", now);
        assertNotSame(a, b);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        List<HistoryEntry> list = Arrays.asList(a, new HistoryEntry(2L, "6 × 7", "42", now));
        String exported = HistoryText.toText(list);
        assertEquals("2 + 3 × 4 = 14\n6 × 7 = 42\n", exported);
    }
}
