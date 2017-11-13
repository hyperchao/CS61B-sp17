package db;

import com.sun.deploy.security.ValidationState;

public class StringValue implements Value {
    public final String val;
    public final Type type;
    public static final StringValue novalue = new StringValue(null, Type.NOVALUE);
    public static final StringValue zero = new StringValue("", Type.STRING);

    public StringValue(String val, Type type) {
        this.val = val;
        this.type = type;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public boolean compare(CompareOp cmp, Value v) {
        if (type == Type.NOVALUE || v.type() == Type.NOVALUE) {
            return false;
        }
        String str = ((StringValue)v).val;
        switch (cmp) {
            case EQ:    return val.compareTo(str) == 0;
            case NE:    return val.compareTo(str) != 0;
            case GE:    return val.compareTo(str) >= 0;
            case LE:    return val.compareTo(str) <= 0;
            case GT:    return val.compareTo(str) > 0;
            case LT:    return val.compareTo(str) < 0;
        }
        return false;
    }

    @Override
    public Value compute(char operator, Value v) {
        if (operator != '+') {
            return null;
        }
        if (v == novalue && this == novalue) {
            return novalue;
        }
        if (v == novalue) {
            v = zero;
        }
        StringValue self = this;
        if (self == novalue) {
            self = zero;
        }

        return new StringValue(val.concat(((StringValue)v).val), Type.STRING);
    }

    @Override
    public String toString() {
        if (type == Type.NOVALUE) {
            return type.toString();
        }
        return "'" + val + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        StringValue v = (StringValue)o;
        return type == v.type && val.equals(v.val);
    }
}
