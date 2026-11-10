package vn.edu.course.calculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculatorViewModel extends ViewModel {

    private final MutableLiveData<CalculatorUiState> state = new MutableLiveData<>(new CalculatorUiState());

    @NonNull
    public LiveData<CalculatorUiState> getState() {
        return state;
    }

    @NonNull
    public CalculatorUiState getCurrentState() {
        CalculatorUiState current = state.getValue();
        return current != null ? current : new CalculatorUiState();
    }

    public void restoreExpressionAndResult(@NonNull String expression, @NonNull String result) {
        CalculatorUiState current = getCurrentState();
        if (current.getExpression().isEmpty() && current.getResult().isEmpty()
                && (!expression.isEmpty() || !result.isEmpty())) {
            state.setValue(current.withExpressionAndResult(expression, result));
        }
    }

    public void onAppendNumber(@NonNull String number, @NonNull String divideByZeroMsg) {
        CalculatorUiState current = getCurrentState();
        String newExpr = current.getExpression() + number;
        String newRes = previewResult(newExpr, current.getResult(), divideByZeroMsg);
        state.setValue(current.withExpressionAndResult(newExpr, newRes));
    }

    public void onAppendDot() {
        CalculatorUiState current = getCurrentState();
        String expr = current.getExpression();
        if (expr.isEmpty()) {
            expr = "0.";
        } else {
            int lastSpace = expr.lastIndexOf(' ');
            String currentNumber = (lastSpace == -1) ? expr : expr.substring(lastSpace + 1);
            if (!currentNumber.contains(".")) {
                if (currentNumber.isEmpty()) {
                    expr += "0.";
                } else {
                    expr += ".";
                }
            }
        }
        state.setValue(current.withExpression(expr));
    }

    public void onAppendOperator(@NonNull String operator, @NonNull String divideByZeroMsg, @NonNull String invalidMsg) {
        CalculatorUiState current = getCurrentState();
        String expr = current.getExpression();
        String res = current.getResult();

        if (expr.isEmpty()) {
            if (!res.isEmpty() && !res.equals(divideByZeroMsg) && !res.equals(invalidMsg)) {
                expr = res + " " + operator + " ";
                res = "";
            } else if (operator.equals("−")) {
                expr = "−";
            }
            state.setValue(current.withExpressionAndResult(expr, res));
            return;
        }

        String trimmed = expr.trim();
        if (trimmed.endsWith("÷") || trimmed.endsWith("×") || trimmed.endsWith("−") || trimmed.endsWith("+")) {
            int lastOpIdx = expr.lastIndexOf(trimmed.substring(trimmed.length() - 1));
            expr = expr.substring(0, lastOpIdx).trim() + " " + operator + " ";
        } else {
            expr = expr + " " + operator + " ";
        }
        state.setValue(current.withExpression(expr));
    }

    public void onDelete(@NonNull String divideByZeroMsg) {
        CalculatorUiState current = getCurrentState();
        String expr = current.getExpression();
        if (expr.isEmpty()) {
            return;
        }

        if (expr.endsWith(" ")) {
            int lastSpace = expr.trim().lastIndexOf(' ');
            if (lastSpace == -1) {
                expr = "";
            } else {
                expr = expr.substring(0, lastSpace + 1);
            }
        } else {
            expr = expr.substring(0, expr.length() - 1);
        }

        if (expr.endsWith(" ")) {
            expr = expr.trim();
        }

        String newRes = expr.isEmpty() ? "" : previewResult(expr, "", divideByZeroMsg);
        state.setValue(current.withExpressionAndResult(expr, newRes));
    }

    public void onClear() {
        CalculatorUiState current = getCurrentState();
        state.setValue(current.withExpressionAndResult("", ""));
    }

    public void onEvaluate(@NonNull String divideByZeroMsg, @NonNull String invalidMsg) {
        CalculatorUiState current = getCurrentState();
        String expr = current.getExpression().trim();
        if (expr.isEmpty()) {
            return;
        }

        CalculatorEvaluator.EvaluationResult eval = CalculatorEvaluator.evaluate(expr);
        if (eval.isSuccess()) {
            String evaluatedResult = eval.getResult();
            List<HistoryEntry> updatedHistory = new ArrayList<>(current.getHistory());
            updatedHistory.add(0, new HistoryEntry(expr, evaluatedResult));
            state.setValue(new CalculatorUiState(expr, evaluatedResult, updatedHistory));
        } else if (eval.isDivideByZero()) {
            state.setValue(current.withResult(divideByZeroMsg));
        } else {
            state.setValue(current.withResult(invalidMsg));
        }
    }

    public void onClearHistory() {
        state.setValue(getCurrentState().withHistory(Collections.emptyList()));
    }

    public void onRestoreHistory(@NonNull List<HistoryEntry> removed) {
        state.setValue(getCurrentState().withHistory(removed));
    }

    @Nullable
    public HistoryEntry entryById(long id) {
        for (HistoryEntry entry : getCurrentState().getHistory()) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        return null;
    }

    public void onUseHistoryEntry(@NonNull HistoryEntry entry) {
        state.setValue(getCurrentState().withExpressionAndResult(entry.getExpression(), entry.getResult()));
    }

    @NonNull
    private String previewResult(@NonNull String expr, @NonNull String fallbackResult, @NonNull String divideByZeroMsg) {
        if (expr.contains("+") || expr.contains("−") || expr.contains("×") || expr.contains("÷")) {
            CalculatorEvaluator.EvaluationResult eval = CalculatorEvaluator.evaluate(expr);
            if (eval.isSuccess()) {
                return eval.getResult();
            } else if (eval.isDivideByZero()) {
                return divideByZeroMsg;
            }
        }
        return fallbackResult;
    }
}
