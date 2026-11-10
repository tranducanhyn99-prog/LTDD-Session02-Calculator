package vn.edu.course.calculator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import vn.edu.course.calculator.databinding.FragmentHistoryDetailBinding;

public class HistoryDetailFragment extends Fragment {

    @Nullable
    private FragmentHistoryDetailBinding binding;

    public HistoryDetailFragment() {
        super(R.layout.fragment_history_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHistoryDetailBinding.bind(view);

        long id = HistoryDetailFragmentArgs.fromBundle(requireArguments()).getEntryId();
        CalculatorViewModel viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);
        HistoryEntry entry = viewModel.entryById(id);

        if (entry == null) {
            NavHostFragment.findNavController(this).popBackStack();
            return;
        }

        binding.expression.setText(entry.getExpression());
        binding.result.setText(entry.getResult());
        binding.time.setText(DateUtils.getRelativeTimeSpanString(entry.getAt().toEpochMilli()));

        binding.share.setOnClickListener(v -> shareResult(entry));
        binding.copy.setOnClickListener(v -> copyResult(entry));
        binding.useResult.setOnClickListener(v -> {
            viewModel.onUseHistoryEntry(entry);
            NavHostFragment.findNavController(this).navigate(R.id.calculatorFragment);
        });
    }

    private void shareResult(@NonNull HistoryEntry entry) {
        String text = getString(R.string.share_text, entry.getExpression(), entry.getResult());
        Intent send = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(send, null));
    }

    private void copyResult(@NonNull HistoryEntry entry) {
        ClipboardManager clipboard = requireContext().getSystemService(ClipboardManager.class);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("result", entry.getResult()));
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                showMessage(R.string.copied);
            }
        }
    }

    private void showMessage(@StringRes int resId) {
        if (binding != null) {
            Snackbar.make(binding.getRoot(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
