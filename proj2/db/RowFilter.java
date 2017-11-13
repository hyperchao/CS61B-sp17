package db;

public class RowFilter {
    public final Column col;
    public final CompareOp cmp;
    public final Column other;
    public final Value literal;

    public RowFilter(Column col, CompareOp cmp, Column other) {
        this.col = col;
        this.cmp = cmp;
        this.other = other;
        literal = null;
    }

    public RowFilter(Column col, CompareOp cmp, Value literal) {
        this.col = col;
        this.cmp = cmp;
        this.literal = literal;
        other = null;
    }

}
