package db;

public class FloatValue implements Value {
    public final double val;
    public final Type type;
    public static final FloatValue nan = new FloatValue(0, Type.NaN);
    public static final FloatValue novalue = new FloatValue(0, Type.NOVALUE);
    public static final FloatValue zero = new FloatValue(0, Type.FLOAT);

    public FloatValue(double val, Type type) {
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
        if (v.type() == Type.NaN) {
            return (type != Type.NaN)?(cmp == CompareOp.LT || cmp == CompareOp.NE):
                    (cmp == CompareOp.GE || cmp == CompareOp.LE || cmp == CompareOp.EQ);
        }
        if (type == Type.NaN) {
            return cmp == CompareOp.GT || cmp == CompareOp.NE;
        }
        double fval = (v.type() == Type.FLOAT)?((FloatValue)v).val:((IntValue)v).val;
        switch (cmp) {
            case EQ:    return val == fval;
            case NE:    return val != fval;
            case GE:    return val >= fval;
            case LE:    return val <= fval;
            case GT:    return val >  fval;
            case LT:    return val <  fval;
        }
        return false;
    }

    @Override
    public Value compute(char operator, Value v) {
        if (type == Type.NaN || v.type() == Type.NaN) {
            return FloatValue.nan;
        }
        if (type == Type.NOVALUE && v.type() == Type.NOVALUE) {
            return FloatValue.novalue;
        }
        // treat novalue as zero
        FloatValue self = this;
        if (self == novalue) {
            self = FloatValue.zero;
        }
        if (v == novalue) {
            v = FloatValue.zero;
        }
        else if (v == IntValue.novalue) {
            v = IntValue.zero;
        }
        double fval = (v.type()==Type.FLOAT)?((FloatValue)v).val:((IntValue)v).val;
        switch (operator) {
            case '+':   return new FloatValue(self.val + fval, Type.FLOAT);
            case '-':   return new FloatValue(self.val - fval, Type.FLOAT);
            case '*':   return new FloatValue(self.val * fval, Type.FLOAT);
            case '/':    return (fval == 0)?FloatValue.nan:new FloatValue(self.val / fval, Type.FLOAT);
        }
        return null;
    }

    @Override
    public String toString() {
        if (type == Type.NOVALUE || type == Type.NaN) {
            return type.toString();
        }
        return String.format("%.3f", val);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        FloatValue v = (FloatValue)o;
        return type == v.type && val == v.val;
    }
}
