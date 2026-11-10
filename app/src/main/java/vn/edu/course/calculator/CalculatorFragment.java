package vn.edu.course.calculator;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.course.calculator.databinding.FragmentCalculatorBinding;

public class CalculatorFragment extends Fragment {
    private static final String KEY_EXPRESSION = "key_expression";
    private static final String KEY_RESULT = "key_result";

    // Valid only between onViewCreated and onDestroyView (Slide 12)
    @Nullable
    private FragmentCalculatorBinding binding;
    private CalculatorViewModel viewModel;

    public CalculatorFragment() {
        super(R.layout.fragment_calculator);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCalculatorBinding.bind(view);
        viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);

        if (savedInstanceState != null) {
            String savedExpr = savedInstanceState.getString(KEY_EXPRESSION, "");
            String savedRes = savedInstanceState.getString(KEY_RESULT, "");
            viewModel.restoreExpressionAndResult(savedExpr, savedRes);
        }

        bindKeys();
        viewModel.getState().observe(getViewLifecycleOwner(), this::render);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel != null) {
            CalculatorUiState current = viewModel.getCurrentState();
            outState.putString(KEY_EXPRESSION, current.getExpression());
            outState.putString(KEY_RESULT, current.getResult());
        }
    }

    private void bindKeys() {
        if (binding == null) return;
        String divZero = getString(R.string.error_divide_by_zero);
        String invalid = getString(R.string.error_invalid_expression);

        binding.button0.setOnClickListener(v -> viewModel.onAppendNumber("0", divZero));
        binding.button1.setOnClickListener(v -> viewModel.onAppendNumber("1", divZero));
        binding.button2.setOnClickListener(v -> viewModel.onAppendNumber("2", divZero));
        binding.button3.setOnClickListener(v -> viewModel.onAppendNumber("3", divZero));
        binding.button4.setOnClickListener(v -> viewModel.onAppendNumber("4", divZero));
        binding.button5.setOnClickListener(v -> viewModel.onAppendNumber("5", divZero));
        binding.button6.setOnClickListener(v -> viewModel.onAppendNumber("6", divZero));
        binding.button7.setOnClickListener(v -> viewModel.onAppendNumber("7", divZero));
        binding.button8.setOnClickListener(v -> viewModel.onAppendNumber("8", divZero));
        binding.button9.setOnClickListener(v -> viewModel.onAppendNumber("9", divZero));
        binding.buttonDot.setOnClickListener(v -> viewModel.onAppendDot());

        binding.buttonDivide.setOnClickListener(v -> viewModel.onAppendOperator("÷", divZero, invalid));
        binding.buttonMultiply.setOnClickListener(v -> viewModel.onAppendOperator("×", divZero, invalid));
        binding.buttonMinus.setOnClickListener(v -> viewModel.onAppendOperator("−", divZero, invalid));
        binding.buttonPlus.setOnClickListener(v -> viewModel.onAppendOperator("+", divZero, invalid));

        binding.buttonClear.setOnClickListener(v -> viewModel.onClear());
        binding.buttonDelete.setOnClickListener(v -> viewModel.onDelete(divZero));
        binding.buttonDelete.setOnLongClickListener(v -> {
            viewModel.onClear();
            return true;
        });

        binding.buttonEquals.setOnClickListener(v -> viewModel.onEvaluate(divZero, invalid));
    }

    private void render(@NonNull CalculatorUiState state) {
        if (binding == null) return;
        binding.expressionText.setText(state.getExpression());
        binding.resultText.setText(state.getResult());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
