package db;

public interface Value {

    enum Type {STRING, FLOAT, INT, NaN, NOVALUE}

    Type type();

    Value compute(char operator, Value v);

    boolean compare(CompareOp cmp, Value v);

    @Override
    String toString();

    @Override
    boolean equals(Object o);
}