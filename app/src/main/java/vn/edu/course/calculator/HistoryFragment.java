package vn.edu.course.calculator;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import vn.edu.course.calculator.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    // Valid only between onViewCreated and onDestroyView (Slide 12)
    @Nullable
    private FragmentHistoryBinding binding;
    private CalculatorViewModel viewModel;

    // Activity Result API contract registered in field initializer (Slide 10)
    private final ActivityResultLauncher<String> exportHistory =
            registerForActivityResult(
                    new ActivityResultContracts.CreateDocument("text/plain"),
                    this::writeHistory);

    public HistoryFragment() {
        super(R.layout.fragment_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHistoryBinding.bind(view);
        viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);

        HistoryAdapter adapter = new HistoryAdapter(entry -> openDetail(entry.getId()));
        int columns = getResources().getInteger(R.integer.history_columns);
        binding.historyList.setLayoutManager(new GridLayoutManager(requireContext(), columns));
        binding.historyList.setAdapter(adapter);

        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (binding == null) return;
            List<HistoryEntry> history = state.getHistory();
            adapter.submitList(new ArrayList<>(history));
            boolean isEmpty = history.isEmpty();
            binding.emptyText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.clear.setEnabled(!isEmpty);
            binding.export.setEnabled(!isEmpty);
        });

        binding.clear.setOnClickListener(v -> clearHistory());
        binding.export.setOnClickListener(v -> onExportClicked());
    }

    private void openDetail(long entryId) {
        NavDirections action = HistoryFragmentDirections.actionHistoryToDetail(entryId);
        NavHostFragment.findNavController(this).navigate(action);
    }

    private void clearHistory() {
        if (binding == null) return;
        List<HistoryEntry> removed = new ArrayList<>(viewModel.getCurrentState().getHistory());
        if (removed.isEmpty()) return;

        viewModel.onClearHistory();
        Snackbar.make(binding.getRoot(), R.string.history_cleared, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> viewModel.onRestoreHistory(removed))
                .show();
    }

    private void onExportClicked() {
        exportHistory.launch("calculator-history.txt");
    }

    private void writeHistory(@Nullable Uri uri) {
        if (uri == null) return; // User backed out of picker
        String text = HistoryText.toText(viewModel.getCurrentState().getHistory());
        try (OutputStream out = requireContext().getContentResolver().openOutputStream(uri)) {
            if (out != null) {
                out.write(text.getBytes(StandardCharsets.UTF_8));
                showMessage(R.string.export_success);
            }
        } catch (IOException e) {
            showMessage(R.string.export_failed);
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
