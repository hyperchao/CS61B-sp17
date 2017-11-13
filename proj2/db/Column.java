package db;

import java.util.ArrayList;
import java.util.List;

public class Column {
    public final Attribute attr;
    List<Value> colVals;

    public Column(String name, Value.Type type) {
        attr = new Attribute(name, type);
        colVals = new ArrayList<>();
    }

    public int size() {
        return colVals.size();
    }

    public String name() {
        return attr.name;
    }

    public Value.Type type() {
        return attr.type;
    }

    public boolean add(Value v) {
        return colVals.add(v);
    }

    public Value get(int i) {
        return colVals.get(i);
    }

    public Column compute(char operator, Value literal, String as) throws Exception {
        Value.Type type = type();
        if (type == Value.Type.STRING && (literal.type() != Value.Type.STRING || operator != '+')) {
            throw new Exception(
                    String.format("unsupported expression %s %c %s", type.toString(), operator, literal.type().toString()));
        }
        if (literal.type() == Value.Type.FLOAT) {
            type = Value.Type.FLOAT;
        }
        Column rstCol = new Column(as, type);
        for (Value val : colVals) {
            rstCol.add(val.compute(operator, literal));
        }
        return rstCol;
    }

    public Column compute(char operator, Column col, String as) throws Exception {
        Value.Type type = type();
        if (type == Value.Type.STRING && (col.type() != Value.Type.STRING || operator != '+')) {
            throw new Exception(
                    String.format("unsupported expression %s %c %s", type.toString(), operator, col.type().toString()));
        }
        if (col.type() == Value.Type.FLOAT) {
            type = Value.Type.FLOAT;
        }
        Column rstCol = new Column(as, type);
        for (int i = 0; i < size(); i += 1) {
            rstCol.add(get(i).compute(operator, col.get(i)));
        }
        return rstCol;
    }

    // return a new Column with the name as
    public Column rename(String as) {
        if (as == null) {
            as = name();
        }
        Column col = new Column(as, type());
        for (Value val : colVals) {
            col.add(val);
        }
        return col;
    }
}
