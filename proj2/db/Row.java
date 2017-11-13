package db;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Row {
    private List<Value> rowVals;

    public Row() {
        rowVals = new ArrayList<>();
    }

    public int size() {
        return rowVals.size();
    }

    public boolean add(Value v) {
        return rowVals.add(v);
    }

    public Value get(int i) {
        return rowVals.get(i);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        for (Value val : rowVals) {
            joiner.add(val.toString());
        }
        return joiner.toString();
    }

    public Row concat(Row row) {
//        Row result = new Row();
//        for (Value val : rowVals) {
//            result.add(val);
//        }
        Row result = clone();
        for (Value val : row.rowVals) {
            result.add(val);
        }
        return result;
    }

    @Override
    public Row clone() {
        Row result = new Row();
        for (Value val : rowVals) {
            result.add(val);
        }
        return result;
    }

}
