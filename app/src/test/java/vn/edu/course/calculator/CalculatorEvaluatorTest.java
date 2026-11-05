package vn.edu.course.calculator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorEvaluatorTest {

    @Test
    public void testOperatorPrecedence() {
        // Acceptance criteria from Slide 34, 35: 2 + 3 * 4 = 14
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
}
