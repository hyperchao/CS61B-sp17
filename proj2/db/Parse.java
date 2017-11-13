package db;

import java.util.regex.Pattern;

public class Parse {
    // Various common constructs, simplifies parsing.
    public static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";
    // Stage 1 syntax, contains the command name.
    public static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);
    // Stage 2 syntax, contains the clauses of commands.
    public static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s*\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");
    // column expression syntax and others
    public static final Pattern COLUMN_EXPR = Pattern.compile("(\\w+)\\s*(?:([+\\-*/])\\s*(\\S+|'.*'))?(?:\\s+as\\s+(\\S+))?");
    public static final Pattern FILTER_EXPR = Pattern.compile("(\\w+)\\s*(==|!=|<=|>=|<|>)\\s*(\\S+|'.*')");


    public static boolean isInt(String str) {
        return str.matches("[+\\-]?\\d+");
    }

    public static boolean isFloat(String str) {
        return str.matches("[+\\-]?\\d+\\.\\d+");
    }

    public static boolean isString(String str) {
        return str.matches("'.*'");
    }

    public static Value toInt(String str) {
        if (str.equals("NOVALUE")) {
            return IntValue.novalue;
        }
        if (str.equals("NaN")) {
            return IntValue.nan;
        }
        return new IntValue(Integer.parseInt(str), Value.Type.INT);
    }

    public static Value toFloat(String str) {
        if (str.equals("NOVALUE")) {
            return FloatValue.novalue;
        }
        if (str.equals("NaN")) {
            return FloatValue.nan;
        }
        return  new FloatValue(Double.parseDouble(str), Value.Type.FLOAT);
    }

    public static Value toStr(String str) throws RuntimeException {
        if (str.equals("NOVALUE")) {
            return StringValue.novalue;
        }
        if (!isString(str)) {
            throw new RuntimeException("invalid string format");
        }
        return new StringValue(str.substring(1, str.length() - 1), Value.Type.STRING);
    }

    // return literal value of str, eg. 1.1 = FloatValue, 'lily' = StringValue and 0 = IntValue
    // if cannot parse to a literal, return null
    public static Value toLiteral(String str) {
        if (isInt(str)) {
            return toInt(str);
        }
        if (isFloat(str)) {
            return toFloat(str);
        }
        if (isString(str)) {
            return toStr(str);
        }
        return null;
    }

    public static CompareOp toCmpOp(String str) {
        if (str.equals("==")) {
            return CompareOp.EQ;
        }
        else if (str.equals("!=")) {
            return CompareOp.NE;
        }
        else if (str.equals("<=")) {
            return CompareOp.LE;
        }
        else if (str.equals(">=")) {
            return CompareOp.GE;
        }
        else if (str.equals("<")) {
            return CompareOp.LT;
        }
        else {
            return CompareOp.GT;
        }
    }
}
