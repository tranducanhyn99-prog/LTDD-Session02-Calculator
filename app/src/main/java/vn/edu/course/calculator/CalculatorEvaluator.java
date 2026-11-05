package vn.edu.course.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CalculatorEvaluator {

    public static class EvaluationResult {
        private final boolean success;
        private final String result;
        private final boolean divideByZero;

        private EvaluationResult(boolean success, String result, boolean divideByZero) {
            this.success = success;
            this.result = result;
            this.divideByZero = divideByZero;
        }

        public static EvaluationResult success(String result) {
            return new EvaluationResult(true, result, false);
        }

        public static EvaluationResult divideByZero() {
            return new EvaluationResult(false, null, true);
        }

        public static EvaluationResult invalid() {
            return new EvaluationResult(false, null, false);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getResult() {
            return result;
        }

        public boolean isDivideByZero() {
            return divideByZero;
        }
    }

    /**
     * Evaluates a mathematical expression respecting operator precedence (× and ÷ before + and −).
     * Example: "2 + 3 × 4" -> 14
     */
    public static EvaluationResult evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return EvaluationResult.invalid();
        }

        // Standardize operators: replace ASCII * and / with display symbols × and ÷ if any
        String normalized = expression.replace("*", "×")
                .replace("/", "÷")
                .replace("-", "−")
                .trim();

        List<String> tokens = tokenize(normalized);
        if (tokens.isEmpty()) {
            return EvaluationResult.invalid();
        }

        // Expression cannot start or end with an operator (except unary minus on first number)
        String lastToken = tokens.get(tokens.size() - 1);
        if (isOperator(lastToken)) {
            return EvaluationResult.invalid();
        }

        try {
            // First pass: Process higher precedence operators: × and ÷
            List<String> firstPassTokens = new ArrayList<>();
            int i = 0;
            while (i < tokens.size()) {
                String token = tokens.get(i);
                if (token.equals("×") || token.equals("÷")) {
                    if (firstPassTokens.isEmpty() || i + 1 >= tokens.size()) {
                        return EvaluationResult.invalid();
                    }
                    BigDecimal left = new BigDecimal(firstPassTokens.remove(firstPassTokens.size() - 1));
                    String nextToken = tokens.get(i + 1);
                    if (isOperator(nextToken)) {
                        return EvaluationResult.invalid();
                    }
                    BigDecimal right = new BigDecimal(nextToken);

                    BigDecimal intermediate;
                    if (token.equals("×")) {
                        intermediate = left.multiply(right);
                    } else {
                        if (right.compareTo(BigDecimal.ZERO) == 0) {
                            return EvaluationResult.divideByZero();
                        }
                        intermediate = left.divide(right, 10, RoundingMode.HALF_UP);
                    }
                    firstPassTokens.add(intermediate.stripTrailingZeros().toPlainString());
                    i += 2;
                } else {
                    firstPassTokens.add(token);
                    i++;
                }
            }

            // Second pass: Process + and −
            if (firstPassTokens.isEmpty()) {
                return EvaluationResult.invalid();
            }

            BigDecimal total = new BigDecimal(firstPassTokens.get(0));
            i = 1;
            while (i < firstPassTokens.size()) {
                String op = firstPassTokens.get(i);
                if (i + 1 >= firstPassTokens.size()) {
                    return EvaluationResult.invalid();
                }
                BigDecimal nextVal = new BigDecimal(firstPassTokens.get(i + 1));
                if (op.equals("+")) {
                    total = total.add(nextVal);
                } else if (op.equals("−")) {
                    total = total.subtract(nextVal);
                } else {
                    return EvaluationResult.invalid();
                }
                i += 2;
            }

            // Format result: strip trailing zeros and avoid scientific notation for readable numbers
            total = total.stripTrailingZeros();
            String formatted = total.toPlainString();
            if (formatted.equals("-0")) {
                formatted = "0";
            }
            return EvaluationResult.success(formatted);

        } catch (ArithmeticException e) {
            return EvaluationResult.divideByZero();
        } catch (Exception e) {
            return EvaluationResult.invalid();
        }
    }

    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentNumber = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (c == '+' || c == '−' || c == '×' || c == '÷') {
                // Check if this is a leading negative sign for the first number or after an operator
                if (c == '−' && currentNumber.length() == 0 && (tokens.isEmpty() || isOperator(tokens.get(tokens.size() - 1)))) {
                    currentNumber.append("-");
                    continue;
                }

                if (currentNumber.length() > 0) {
                    tokens.add(currentNumber.toString());
                    currentNumber.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (Character.isDigit(c) || c == '.') {
                currentNumber.append(c);
            } else {
                // Ignore unexpected characters
            }
        }

        if (currentNumber.length() > 0) {
            tokens.add(currentNumber.toString());
        }

        return tokens;
    }

    public static boolean isOperator(String token) {
        return "+".equals(token) || "−".equals(token) || "×".equals(token) || "÷".equals(token);
    }
}
