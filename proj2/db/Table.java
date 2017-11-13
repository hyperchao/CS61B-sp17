package db;

import java.util.*;
import java.util.regex.Matcher;

public  class Table {
    public String name;
    private List<Column> cols;
    private List<Row> rows;
    private int colNum, rowNum;
    private Map<String, Column> colMap;

    private Table(String name) {
        this.name = name;
        cols = new ArrayList<>();
        rows = new ArrayList<>();
        colMap = new HashMap<>();
        colNum = rowNum = 0;
    }

    private Table emptyClone() {
        Table t = new Table("");
        for (Column col : cols) {
            t.addColumn(new Column(col.name(), col.type()));
        }
        return t;
    }

    private boolean addColumn(Column col) {
        if (col == null || colNum != 0 && col.size() != rowNum) {
            return false;
        }
        cols.add(col);
        colMap.put(col.name(), col);
        for (int i = 0; i < col.size(); i += 1) {
            if (colNum == 0) {
                Row row = new Row();
                row.add(col.get(i));
                rows.add(row);
            }
            else {
                rows.get(i).add(col.get(i));
            }
        }
        if (colNum == 0) {
            rowNum = rows.size();
        }
        colNum += 1;
        return true;
    }

    private boolean addRow(Row row) {
        if (row == null || row.size() != colNum) {
            return false;
        }
        rows.add(row);
        for (int i = 0; i < row.size(); i += 1) {
            cols.get(i).add(row.get(i));
        }
        rowNum += 1;
        return true;
    }

    /**
     * create a new Table with giving name and description.
     * @param name the table name
     * @param attributeStr a string of pattern "columnName0 type0,columnName1 type1,..."
     * @return a Table created with name and description.
     */
    public static Table create(String name, String attributeStr) throws Exception {
        String[] attrStrs = attributeStr.split(Parse.COMMA);
        Table table = new Table(name);
        for (String str : attrStrs) {
            String[] attr = str.split("\\s+");
            if (attr[1].equals("string")) {
                table.addColumn(new Column(attr[0], Value.Type.STRING));
            }
            else if (attr[1].equals("int")) {
                table.addColumn(new Column(attr[0], Value.Type.INT));
            }
            else if (attr[1].equals("float")) {
                table.addColumn(new Column(attr[0], Value.Type.FLOAT));
            }
            else {
                throw new Exception("unknown type: " + attr[1]);
            }
        }
        return table;
    }

    /**
     * insert a tuple into the table
     * @param valueStr a string of pattern "value0,value1, ...". no extra spaces
     * @return return true if insert successfully, false otherwise
     */
    public void insertRow(String valueStr) throws Exception {
        String[] vals = valueStr.split(Parse.COMMA);
        if (vals.length != colNum) {
            throw new Exception("insert number not equal column number");
        }
        Row row = new Row();
        for (int i = 0; i < colNum; i += 1) {
            try {
                switch (cols.get(i).type()) {
                    case INT:
                        row.add(Parse.toInt(vals[i]));
                        break;
                    case FLOAT:
                        row.add(Parse.toFloat(vals[i]));
                        break;
                    case STRING:
                        row.add(Parse.toStr(vals[i]));
                }
            }
            catch (Exception e) {
                throw new Exception(String.format("error: %s to %s", vals[i], cols.get(i).type().toString()));
            }
        }
        addRow(row);
    }

    /**
     * return the String represent of the table, which has the same format of .tbl files.
     * @return return the String represent of the table.
     */
    public String print() {
        StringJoiner joiner = new StringJoiner("\n");
        StringJoiner attrJoiner = new StringJoiner(",");
        for (Column col : cols) {
            attrJoiner.add(col.name() + " " + col.type().toString().toLowerCase());
        }
        joiner.add(attrJoiner.toString());
        for (Row row : rows) {
            joiner.add(row.toString());
        }

        return joiner.toString();
    }

    /**
     * generate table from string
     * @param name the table name
     * @param content the content of a .tbl file, represent in string
     * @return generated table, if something went wrong, return null
     */
    public static Table read(String name, String content) throws Exception {
        String[] strs = content.split("\n");
        Table table;
        try {
            table = Table.create(name, strs[0]);
            for (int i = 1; i < strs.length; i += 1) {
                table.insertRow(strs[i]);
            }
        }
        catch (Exception e) {
            throw new Exception("Table is broken: " + name);
        }
        return table;
    }

    public Table join(Table otherTable) {
        JoinHelper helper = preJoin(otherTable);
        Table table = new Table("");

        if (helper.mutual) { // if there are attributes in common, make natural join
            mergeAttr(table, otherTable, helper);
            mergeRows(table, otherTable, helper);
        }
        else { // if no attributes in common, make Cartesian Product
            for (Column self : cols) {
                table.addColumn(new Column(self.name(), self.type()));
            }
            for (Column other : otherTable.cols) {
                table.addColumn(new Column(other.name(), other.type()));
            }
            for (Row self : rows) {
                for (Row other : otherTable.rows) {
                    table.addRow(self.concat(other));
                }
            }
        }

        return table;
    }

    private JoinHelper preJoin(Table t) {
        JoinHelper helper = new JoinHelper();
        for (int i = 0; i < cols.size(); i += 1) {
            for (int j = 0; j < t.cols.size(); j += 1) {
                if (cols.get(i).attr.equals(t.cols.get(j).attr)) {
                    helper.selfList.add(i);
                    helper.selfSet.add(i);
                    helper.otherList.add(j);
                    helper.otherSet.add(j);
                }
            }
        }
        helper.mutual = !helper.selfList.isEmpty();
        return helper;
    }

    private void mergeAttr(Table table, Table other, JoinHelper helper) {
        int n = helper.selfList.size();
        for (int i = 0; i < n; i += 1) {
            Column col = cols.get(helper.selfList.get(i));
            table.addColumn(new Column(col.name(), col.type()));
        }
        for (int i = 0; i < cols.size(); i += 1) {
            if (!helper.selfSet.contains(i)) {
                Column col = cols.get(i);
                table.addColumn(new Column(col.name(), col.type()));
            }
        }
        for (int i = 0; i < other.cols.size(); i += 1) {
            if (!helper.otherSet.contains(i)) {
                Column col = other.cols.get(i);
                table.addColumn(new Column(col.name(), col.type()));
            }
        }
    }

    private void mergeRows(Table table, Table other, JoinHelper helper) {
        for (Row selfRow : rows) {
            for (Row otherRow : other.rows) {
                boolean merge = true;
                Row row = new Row();
                int n = helper.selfList.size();
                for (int i = 0; i < n; i += 1) {
                    Value v1 = selfRow.get(helper.selfList.get(i));
                    Value v2 = otherRow.get(helper.otherList.get(i));
                    if (!v1.equals(v2)) {
                        merge = false;
                        break;
                    }
                    row.add(v1);
                }
                if (merge) {
                    for (int i = 0; i < selfRow.size(); i += 1) {
                        if (!helper.selfSet.contains(i)) {
                            row.add(selfRow.get(i));
                        }
                    }
                    for (int i = 0; i < otherRow.size(); i += 1) {
                        if (!helper.otherSet.contains(i)) {
                            row.add(otherRow.get(i));
                        }
                    }
                    table.addRow(row);
                }
            }
        }
    }

    public Table selectCols(String expr) throws Exception {
        if (expr.equals("*")) {
            return this;
        }
        String[] exprs = expr.split(Parse.COMMA);
        Table rst = new Table("");
        for (String colExpr : exprs) {
            Matcher m = Parse.COLUMN_EXPR.matcher(colExpr);
            if (!m.matches()) {
                throw new Exception("invalid column expression: " + colExpr);
            }
            String name = m.group(1), operator = m.group(2), operand = m.group(3), as = m.group(4);
            if (!colMap.containsKey(name)) {
                throw new Exception("column not exists: " + name);
            }
            Column col = colMap.get(name);
            if (operand != null) {
                if (as == null) {
                    throw new Exception("must specify alias for: " + colExpr);
                }
                Value literal = Parse.toLiteral(operand);
                if (literal != null) {
                    rst.addColumn(col.compute(operator.charAt(0), literal, as));
                }
                else {
                    if (!colMap.containsKey(operand)) {
                        throw new Exception("column not exists: " + operand);
                    }
                    rst.addColumn(col.compute(operator.charAt(0), colMap.get(operand), as));
                }
            }
            else {
                rst.addColumn(col.rename(as));
            }
        }

        return rst;
    }

    public Table filterRows(String expr) throws Exception {
        String[] exprs = expr.split(Parse.AND);
        List<RowFilter> filters = new ArrayList<>();
        for (String filterExpr : exprs) {
            Matcher m = Parse.FILTER_EXPR.matcher(filterExpr);
            if (!m.matches()) {
                throw new Exception("invalid filter expression: " + filterExpr);
            }
            String name = m.group(1), cmpStr = m.group(2), operand = m.group(3);
            CompareOp cmp = Parse.toCmpOp(cmpStr);
            if (!colMap.containsKey(name)) {
                throw new Exception("column not exists: " + name);
            }
            Column col = colMap.get(name);
            Value literal = Parse.toLiteral(operand);
            if (literal != null) {
                if (col.type() != literal.type() &&
                        (col.type() == Value.Type.STRING || literal.type() == Value.Type.STRING)) {
                    throw new Exception(String.format("unsupported comparision: %s %s %s",
                            col.type().toString(), cmpStr, literal.type().toString()));
                }
                filters.add(new RowFilter(col, cmp, literal));
            }
            else {
                if (!colMap.containsKey(operand)) {
                    throw new Exception(String.format("column %s not exists", operand));
                }
                Column other = colMap.get(operand);
                if (col.type() != other.type() &&
                        (col.type() == Value.Type.STRING || other.type() == Value.Type.STRING)) {
                    throw new Exception(String.format("unsupported comparision: %s %s %s",
                            col.type().toString(), cmpStr, literal.type().toString()));
                }
                filters.add(new RowFilter(col, cmp, other));
            }
        }
        Table rst = emptyClone();
        for (int i = 0; i < rows.size(); i += 1) {
            boolean add = true;
            for (RowFilter filter : filters) {
                Value val = (filter.literal == null)?filter.other.get(i):filter.literal;
                if (!filter.col.get(i).compare(filter.cmp, val)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                rst.addRow(rows.get(i).clone());
            }
        }

        return rst;
    }

}