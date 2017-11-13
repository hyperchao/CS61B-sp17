package db;

public class IntValue implements Value {
    public final int val;
    public final Type type;
    public static final IntValue nan = new IntValue(0, Type.NaN);
    public static final IntValue novalue = new IntValue(0, Type.NOVALUE);
    public static final IntValue zero = new IntValue(0, Type.INT);

    public IntValue(int val, Type type) {
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
        // return v if v is FloatValue.nan or IntValue.nan
        if (v.type() == Type.NaN) {
            return v;
        }
        // treat v as zero if v is novalue
        if (v == novalue) {
            if (this == novalue) {
                return novalue;
            }
            v = IntValue.zero;
        }
        else if (v == FloatValue.novalue) {
            if (this == novalue) {
                return v;
            }
            v = FloatValue.zero;
        }
        IntValue self = this;
        // return FloatValue.nan or IntValue.nan according to the type of v;
        if (self == nan) {
            return (v.type() == Type.FLOAT)?FloatValue.nan:IntValue.nan;
        }
        // treat self as zero if self is novalue
        if (self == novalue) {
            self = IntValue.zero;
        }
        // now we are dealing with non-special case
        if (v.type() == Type.FLOAT) {
            FloatValue f = (FloatValue)v;
            switch (operator) {
                case '+':   return new FloatValue(self.val + f.val, Type.FLOAT);
                case '-':   return new FloatValue(self.val - f.val, Type.FLOAT);
                case '*':   return new FloatValue(self.val * f.val, Type.FLOAT);
                case '/':    return (f.val == 0)?FloatValue.nan:new FloatValue(self.val / f.val, Type.FLOAT);
            }
        }
        else {
            IntValue i = (IntValue)v;
            switch (operator) {
                case '+':   return new IntValue(self.val + i.val, Type.INT);
                case '-':   return new IntValue(self.val - i.val, Type.INT);
                case '*':   return new IntValue(self.val * i.val, Type.INT);
                case '/':    return (i.val == 0)?IntValue.nan:new IntValue(self.val / i.val, Type.INT);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (type == Type.NOVALUE || type == Type.NaN) {
            return type.toString();
        }
        return String.valueOf(val);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        IntValue v = (IntValue)o;
        return type == v.type && val == v.val;
    }
}
