package vn.edu.course.calculator;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public final class HistoryEntry {
    private static final AtomicLong NEXT_ID = new AtomicLong(1L);

    private final long id;
    @NonNull
    private final String expression;
    @NonNull
    private final String result;
    @NonNull
    private final Instant at;

    public HistoryEntry(long id, @NonNull String expression, @NonNull String result, @NonNull Instant at) {
        this.id = id;
        this.expression = Objects.requireNonNull(expression);
        this.result = Objects.requireNonNull(result);
        this.at = Objects.requireNonNull(at);
    }

    public HistoryEntry(@NonNull String expression, @NonNull String result) {
        this(NEXT_ID.getAndIncrement(), expression, result, Instant.now());
    }

    public long getId() {
        return id;
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
    public Instant getAt() {
        return at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryEntry)) return false;
        HistoryEntry that = (HistoryEntry) o;
        return id == that.id
                && Objects.equals(expression, that.expression)
                && Objects.equals(result, that.result)
                && Objects.equals(at, that.at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expression, result, at);
    }

    @NonNull
    @Override
    public String toString() {
        return "HistoryEntry(id=" + id + ", expression=" + expression + ", result=" + result + ")";
    }
}
