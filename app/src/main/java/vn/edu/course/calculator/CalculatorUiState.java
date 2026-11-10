package vn.edu.course.calculator;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class CalculatorUiState {
    @NonNull
    private final String expression;
    @NonNull
    private final String result;
    @NonNull
    private final List<HistoryEntry> history;

    public CalculatorUiState() {
        this("", "", Collections.emptyList());
    }

    public CalculatorUiState(@NonNull String expression, @NonNull String result, @NonNull List<HistoryEntry> history) {
        this.expression = Objects.requireNonNull(expression);
        this.result = Objects.requireNonNull(result);
        this.history = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(history)));
    }

    @NonNull
    public String getExpression() {
        return expression;
    }

    @NonNull
    public String getResult() {
        return result;
    }

    @NonNull
    public List<HistoryEntry> getHistory() {
        return history;
    }

    @NonNull
    public CalculatorUiState withExpression(@NonNull String newExpression) {
        return new CalculatorUiState(newExpression, this.result, this.history);
    }

    @NonNull
    public CalculatorUiState withExpressionAndResult(@NonNull String newExpression, @NonNull String newResult) {
        return new CalculatorUiState(newExpression, newResult, this.history);
    }

    @NonNull
    public CalculatorUiState withResult(@NonNull String newResult) {
        return new CalculatorUiState(this.expression, newResult, this.history);
    }

    @NonNull
    public CalculatorUiState withHistory(@NonNull List<HistoryEntry> newHistory) {
        return new CalculatorUiState(this.expression, this.result, newHistory);
    }
}
