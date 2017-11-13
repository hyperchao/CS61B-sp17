package db;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;

public class Database {
    private Map<String, Table> tableMap;

    public Database() {
        tableMap = new HashMap<>();
    }

    public String transact(String query) {
        Matcher m;
        if ((m = Parse.CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        }
        else if ((m = Parse.DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1));
        }
        else if ((m = Parse.LOAD_CMD.matcher(query)).matches()) {
            return loadTable(m.group(1));
        }
        else if ((m = Parse.STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1));
        }
        else if ((m = Parse.INSERT_CMD.matcher(query)).matches()) {
            return insertToTable(m.group(1));
        }
        else if ((m = Parse.SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        }
        else if ((m = Parse.PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1));
        }

        return "Malformed query: " + query;
    }

    private String createTable(String expr) {
        String tableName = expr.split("\\s+")[0];
        if (tableMap.containsKey(tableName)) {
            return "table already exists: " + tableName;
        }

        Matcher m;
        Table table;
        try {
            if ((m = Parse.CREATE_NEW.matcher(expr)).matches()) {
                table = Table.create(m.group(1), m.group(2));
            }
            else if ((m = Parse.CREATE_SEL.matcher(expr)).matches()) {
                table = createSel(expr.split("\\s*as select\\s*")[1]);
                table.name = m.group(1);
            }
            else {
                return "Malformed create: " + expr;
            }
        }
        catch (Exception e) {
            return e.getMessage();
        }
        tableMap.put(m.group(1), table);

        return "";
    }

    private String dropTable(String expr) {
        if (!tableMap.containsKey(expr)) {
            return "table not exists: " + expr;
        }
        tableMap.remove(expr);
        return "";
    }

    private String loadTable(String expr) {
        String path = expr + ".tbl";
        try {
            Scanner scanner = new Scanner(new File(path));
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine() + "\n");
            }
            scanner.close();
            Table table = Table.read(expr, sb.toString());
            tableMap.put(expr, table);
        }
        catch (Exception e) {
            return "file not exists: " + path;
        }

        return "";
    }

    private String printTable(String expr) {
        if (tableMap.containsKey(expr)) {
            return tableMap.get(expr).print();
        }
        return "table not exists: " + expr;
    }

    private Table createSel(String expr) throws Exception {
        Matcher m;
        if (!(m = Parse.SELECT_CLS.matcher(expr)).matches()) {
            throw new Exception("Malformed select: " + expr);
        }
        String[] names = m.group(2).split(Parse.COMMA);
        for (String name : names) {
            if (!tableMap.containsKey(name)) {
                throw new Exception("table not exists: " + name);
            }
        }
        Table table = tableMap.get(names[0]);
        for (int i = 1; i < names.length; i += 1) {
            table = table.join(tableMap.get(names[i]));
        }
        table = table.selectCols(m.group(1));
        if (m.group(3) != null) {
            table = table.filterRows(m.group(3));
        }

         return table;
    }

    private String select(String expr) {
        try {
            return createSel(expr).print();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String insertToTable(String expr) {
        Matcher m;
        if (!(m = Parse.INSERT_CLS.matcher(expr)).matches()) {
            return "Malformed insert: " + expr;
        }
        String name = m.group(1);
        if (!tableMap.containsKey(name)) {
            return "table not exists: " + name;
        }
        try {
            tableMap.get(name).insertRow(m.group(2));
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    private String storeTable(String expr) {
        if (!tableMap.containsKey(expr)) {
            return "table not exists: " + expr;
        }
        String path = expr + ".tbl";
        try {
            FileWriter writer = new FileWriter(new File(path));
            writer.write(tableMap.get(expr).print());
            writer.close();
        }
        catch (Exception e) {
            return "error writing to file: " + path;
        }
        return "";
    }
}
