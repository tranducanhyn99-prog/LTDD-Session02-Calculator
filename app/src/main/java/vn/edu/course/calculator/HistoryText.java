package vn.edu.course.calculator;

import androidx.annotation.NonNull;

import java.util.List;

public final class HistoryText {
    private HistoryText() {
    }

    @NonNull
    public static String toText(@NonNull List<HistoryEntry> history) {
        StringBuilder sb = new StringBuilder();
        for (HistoryEntry entry : history) {
            sb.append(entry.getExpression())
              .append(" = ")
              .append(entry.getResult())
              .append("\n");
        }
        return sb.toString();
    }
}
