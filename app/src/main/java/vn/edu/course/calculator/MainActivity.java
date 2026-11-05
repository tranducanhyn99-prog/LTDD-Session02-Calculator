package vn.edu.course.calculator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import vn.edu.course.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_EXPRESSION = "key_expression";
    private static final String KEY_RESULT = "key_result";
    private static final String KEY_HISTORY = "key_history";

    private ActivityMainBinding binding;

    private String expression = "";
    private String result = "";
    private ArrayList<String> historyList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Set toolbar as action bar for menu support (Slide 30)
        setSupportActionBar(binding.toolbar);

        // Restore saved instance state across orientation change (Slide 34, 35, 37)
        if (savedInstanceState != null) {
            expression = savedInstanceState.getString(KEY_EXPRESSION, "");
            result = savedInstanceState.getString(KEY_RESULT, "");
            ArrayList<String> savedHistory = savedInstanceState.getStringArrayList(KEY_HISTORY);
            if (savedHistory != null) {
                historyList = savedHistory;
            }
        }

        bindKeys();
        render();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_EXPRESSION, expression);
        outState.putString(KEY_RESULT, result);
        outState.putStringArrayList(KEY_HISTORY, historyList);
    }

    /**
     * Wire click and long click listeners using View Binding (Slide 29, 37).
     * No findViewById is used anywhere.
     */
    private void bindKeys() {
        // Number keys
        binding.button0.setOnClickListener(v -> onAppendNumber("0"));
        binding.button1.setOnClickListener(v -> onAppendNumber("1"));
        binding.button2.setOnClickListener(v -> onAppendNumber("2"));
        binding.button3.setOnClickListener(v -> onAppendNumber("3"));
        binding.button4.setOnClickListener(v -> onAppendNumber("4"));
        binding.button5.setOnClickListener(v -> onAppendNumber("5"));
        binding.button6.setOnClickListener(v -> onAppendNumber("6"));
        binding.button7.setOnClickListener(v -> onAppendNumber("7"));
        binding.button8.setOnClickListener(v -> onAppendNumber("8"));
        binding.button9.setOnClickListener(v -> onAppendNumber("9"));
        binding.buttonDot.setOnClickListener(v -> onAppendDot());

        // Operator keys
        binding.buttonDivide.setOnClickListener(v -> onAppendOperator("÷"));
        binding.buttonMultiply.setOnClickListener(v -> onAppendOperator("×"));
        binding.buttonMinus.setOnClickListener(v -> onAppendOperator("−"));
        binding.buttonPlus.setOnClickListener(v -> onAppendOperator("+"));

        // Function keys
        binding.buttonClear.setOnClickListener(v -> onClear());
        binding.buttonDelete.setOnClickListener(v -> onDelete());
        binding.buttonDelete.setOnLongClickListener(v -> {
            onClear();
            return true;
        });

        // Evaluation
        binding.buttonEquals.setOnClickListener(v -> onEvaluate());
    }

    private void onAppendNumber(String number) {
        expression += number;
        autoPreviewResult();
        render();
    }

    private void onAppendDot() {
        if (expression.isEmpty()) {
            expression = "0.";
        } else {
            // Find current number segment
            int lastSpace = expression.lastIndexOf(' ');
            String currentNumber = (lastSpace == -1) ? expression : expression.substring(lastSpace + 1);
            if (!currentNumber.contains(".")) {
                if (currentNumber.isEmpty()) {
                    expression += "0.";
                } else {
                    expression += ".";
                }
            }
        }
        render();
    }

    private void onAppendOperator(String operator) {
        if (expression.isEmpty()) {
            if (!result.isEmpty() && !result.equals(getString(R.string.error_divide_by_zero)) && !result.equals(getString(R.string.error_invalid_expression))) {
                // Continue calculation with previous result
                expression = result + " " + operator + " ";
                result = "";
            } else if (operator.equals("−")) {
                expression = "−";
            }
            render();
            return;
        }

        // If expression ends with an operator, replace it (Slide 29: tap ÷ twice replaces operator)
        String trimmed = expression.trim();
        if (trimmed.endsWith("÷") || trimmed.endsWith("×") || trimmed.endsWith("−") || trimmed.endsWith("+")) {
            int lastOpIdx = expression.lastIndexOf(trimmed.substring(trimmed.length() - 1));
            expression = expression.substring(0, lastOpIdx).trim() + " " + operator + " ";
        } else {
            expression = expression + " " + operator + " ";
        }
        render();
    }

    private void onDelete() {
        if (expression.isEmpty()) {
            return;
        }

        if (expression.endsWith(" ")) {
            // Remove operator with surrounding spaces " + "
            int lastSpace = expression.trim().lastIndexOf(' ');
            if (lastSpace == -1) {
                expression = "";
            } else {
                expression = expression.substring(0, lastSpace + 1);
            }
        } else {
            expression = expression.substring(0, expression.length() - 1);
        }

        if (expression.endsWith(" ")) {
            expression = expression.trim();
        }

        autoPreviewResult();
        render();
    }

    private void onClear() {
        expression = "";
        result = "";
        render();
    }

    private void onEvaluate() {
        if (expression.trim().isEmpty()) {
            return;
        }

        CalculatorEvaluator.EvaluationResult eval = CalculatorEvaluator.evaluate(expression);
        if (eval.isSuccess()) {
            String evaluatedResult = eval.getResult();
            historyList.add(expression + " = " + evaluatedResult);
            result = evaluatedResult;
        } else if (eval.isDivideByZero()) {
            result = getString(R.string.error_divide_by_zero);
        } else {
            result = getString(R.string.error_invalid_expression);
        }
        render();
    }

    private void autoPreviewResult() {
        if (expression.contains("+") || expression.contains("−") || expression.contains("×") || expression.contains("÷")) {
            CalculatorEvaluator.EvaluationResult eval = CalculatorEvaluator.evaluate(expression);
            if (eval.isSuccess()) {
                result = eval.getResult();
            } else if (eval.isDivideByZero()) {
                result = getString(R.string.error_divide_by_zero);
            }
        }
    }

    /**
     * Single render() method updates the display (Slide 37 code review checklist).
     */
    private void render() {
        binding.expressionText.setText(expression.isEmpty() ? "" : expression);
        binding.resultText.setText(result);
    }

    // ================= Options Menu for History (Slide 19, 23, 38) =================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            showHistoryDialog();
            return true;
        } else if (id == R.id.action_clear_history) {
            historyList.clear();
            Toast.makeText(this, R.string.menu_clear_history, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.history_title);

        if (historyList.isEmpty()) {
            builder.setMessage(R.string.history_empty);
        } else {
            CharSequence[] items = historyList.toArray(new CharSequence[0]);
            builder.setItems(items, (dialog, which) -> {
                String chosen = historyList.get(which);
                if (chosen.contains("=")) {
                    String[] parts = chosen.split("=");
                    expression = parts[0].trim();
                    result = parts[1].trim();
                    render();
                }
            });
        }
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }
}
