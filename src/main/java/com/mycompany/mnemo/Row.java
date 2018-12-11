package com.mycompany.mnemo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;


public class Row {
    public Map<String, Object> row;

    public Row() {
        row = new HashMap<String, Object>();
    }

    public void add(String columnName, Object data) {
        row.put(columnName, data);
    }

    // Create a table based on resultSet and save it in given list of rows by reference.
    public static void formTable(ResultSet rs, List<Row> table) throws SQLException {

        // If result set is empty return
        if(rs == null) return;

        // Get information about the result such as column name or type
        ResultSetMetaData rsmd;
        rsmd = rs.getMetaData();

        // Get the number of columns
        int NumOfCol = rsmd.getColumnCount();

        try {
            while (rs.next()) {
                Row current_row = new Row();

                // Iterate over columns and values from result set to the table
                for (int i = 1; i <= NumOfCol; i++) {
                    current_row.add(rsmd.getColumnName(i), rs.getObject(i));
                }
                table.add(current_row);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public String getString(String columnName) {
        return row.get(columnName).toString();
    }

    public Integer getInt(String columnName) {
        return (int) row.get(columnName);
    }

    public Double getDouble(String columnName) {
        return (Double) row.get(columnName);
    }

    public void setValue(String columnName, Object value) {
        row.put(columnName, value);
    }

    public Row getRow() { return this; }
}
