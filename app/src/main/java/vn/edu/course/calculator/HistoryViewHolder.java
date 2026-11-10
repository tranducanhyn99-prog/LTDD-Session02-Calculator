package vn.edu.course.calculator;

import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.course.calculator.databinding.ItemHistoryBinding;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private final ItemHistoryBinding binding;
    private final HistoryAdapter.OnEntryClickListener onClick;

    public HistoryViewHolder(@NonNull ItemHistoryBinding binding,
                             @NonNull HistoryAdapter.OnEntryClickListener onClick) {
        super(binding.getRoot());
        this.binding = binding;
        this.onClick = onClick;
    }

    public void bind(@NonNull HistoryEntry entry) {
        binding.expression.setText(entry.getExpression());
        binding.result.setText(entry.getResult());
        binding.time.setText(DateUtils.getRelativeTimeSpanString(entry.getAt().toEpochMilli()));
        binding.getRoot().setOnClickListener(v -> onClick.onEntryClick(entry));
    }
}
