package vn.edu.course.calculator;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import vn.edu.course.calculator.databinding.ItemHistoryBinding;

public class HistoryAdapter extends ListAdapter<HistoryEntry, HistoryViewHolder> {

    public interface OnEntryClickListener {
        void onEntryClick(@NonNull HistoryEntry entry);
    }

    private final OnEntryClickListener onClick;

    public HistoryAdapter(@NonNull OnEntryClickListener onClick) {
        super(new HistoryDiff());
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(binding, onClick);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static class HistoryDiff extends DiffUtil.ItemCallback<HistoryEntry> {
        @Override
        public boolean areItemsTheSame(@NonNull HistoryEntry a, @NonNull HistoryEntry b) {
            return a.getId() == b.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull HistoryEntry a, @NonNull HistoryEntry b) {
            return a.equals(b);
        }
    }
}
