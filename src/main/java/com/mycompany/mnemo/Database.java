package com.mycompany.mnemo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    // URL for connecting to the database
    private static final String URL = "jdbc:sqlite:decks.db";
    // Name of table containing information about all decks.
    private static final String deckIndexTableName = "deckListTable";



    // Create a table if one doesn't yet exist.
    public static void createTable(String name, String SQLAttributes) {
        try {
            // Connect to the local database and create a statement. If no database, it will be created automatically.
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();

            // Create the table if one doesn't already exist;
            st.execute("CREATE TABLE IF NOT EXISTS " + name + "(" + SQLAttributes + ")");

            // Close the connection
            st.close();
            conn.close();
        }
        catch (SQLException e) {
            System.out.println("Error occurred while creating a table.");
            System.out.println("Error message: " + e.getMessage());
        }
    }
    // If no name has been given use the name stored in deckIndexTableName variable.
    public static void createTable(String SQLAttributes) { createTable(deckIndexTableName, SQLAttributes); }



    // Loads the table from the database, copy the results and return as list of class Row.
    public static List<Row> getTable(String tableName) {
        try {

            // Connect to the local database and create a statement
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();

            // Read the info from given table and store the results
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);

            // Create a list of rows (table) which will be used to store the copy of the database.
            // Using this method we can still access the data even when the connection is closed.
            List<Row> table = new ArrayList<Row>();
            // Fill the table with ResultSet's data
            Row.formTable(rs, table);


            // Close the connection and return table
            rs.close();
            st.close();
            conn.close();

            return table;

        } catch(SQLException e) {
            // Return an empty list and print out error message
            List<Row> table = new ArrayList<Row>();

            System.out.println("Error occurred while loading the data from table: + " + tableName);
            System.out.println("Error message: " + e.getMessage());

            return table ;
        }
    }
    // If no name has been given use the name stored in deckIndexTableName variable.
    public static List<Row> getTable() { return getTable(deckIndexTableName); }



    public static void insertIntoTable(String tableName, String columns, String values) {
        try{
            // Connect to the local database and create a statement
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();

            // Insert data into a table.
            st.execute("INSERT INTO " + tableName + " (" + columns + ")" +
                    " VALUES(" + values + ")");

            // Close the connection and return table
            st.close();
            conn.close();

        } catch(SQLException e) {
            System.out.println("Error occurred while inserting the data into table:  + " + tableName);
            System.out.println("Error message: " + e.getMessage());
        }
    }
    // If no name has been given use the name stored in deckIndexTableName variable.
    public static void insertIntoTable(String columns, String values) { insertIntoTable(deckIndexTableName, columns, values); }


    public static void updateTable(String tableName, String columns, String values, String condition) {
        try{
            // Connect to the local database and create a statement
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();

            System.out.println("UPDATE " + tableName + " SET (" + columns + ") = (" + values + ")" +
                    " " + condition);

            // Insert data into a table.
            st.execute("UPDATE " + tableName + " SET (" + columns + ") = (" + values + ")" +
                    " " + condition);

            // Close the connection and return table
            st.close();
            conn.close();

        } catch(SQLException e) {
            System.out.println("Error occurred while updating the data in table:  + " + tableName);
            System.out.println("Error message: " + e.getMessage());
        }
    }
    // If no name has been given use the name stored in deckIndexTableName variable.
    public static void updateTable(String columns, String values, String condition) {
        updateTable(deckIndexTableName, columns, values, condition);
    }

    public static void updateTable(String tableName, List<Row> table) {
        try{
            // Connect to the local database and create a statement
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();


            // Insert data into a table.
            for(Row row : table) {
                int id = row.getInt("id");
                int lvl = row.getInt("lvl");
                double lastReview = row.getDouble("last_review_time");

                System.out.println("UPDATE " + tableName + " SET ('lvl', 'last_review_time') = (" +
                        "'" + lvl + "', '" + lastReview + "') WHERE id = '" + id + "'");

                st.execute("UPDATE " + tableName + " SET ('lvl', 'last_review_time') = (" +
                        "'" + lvl + "', '" + lastReview + "') WHERE id = '" + id + "'");
            }

            // Close the connection and return table
            st.close();
            conn.close();

        } catch(SQLException e) {
            System.out.println("Error occurred while updating the data in table:  + " + tableName + " with another table");
            System.out.println("Error message: " + e.getMessage());
        }
    }


    public static void dropTable(String tableName) {
        try{
            // Connect to the local database and create a statement
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();

            // Insert data into a table.
            st.execute("DROP TABLE " + tableName);

            // Close the connection and return table
            st.close();
            conn.close();

        } catch(SQLException e) {
            System.out.println("Error occurred while deleting table:  + " + tableName);
            System.out.println("Error message: " + e.getMessage());
        }
    }
    // If no name has been given use the name stored in deckIndexTableName variable.
    public static void dropTable() { dropTable(deckIndexTableName); }


    public static void deleteFromTable(String tableName, String condition) {
        try{
            // Connect to the local database and create a statement
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();

            System.out.println("DELETE FROM " + tableName + " " + condition);
            // Insert data into a table.
            st.execute("DELETE FROM " + tableName + " " + condition);


            // Close the connection and return table
            st.close();
            conn.close();

        } catch(SQLException e) {
            System.out.println("Error occurred while deleting from table: " + tableName);
            System.out.println("Error message: " + e.getMessage());
        }
    }
    // If no name has been given use the name stored in deckIndexTableName variable.
    public static void deleteFromTable(String condition) { deleteFromTable(deckIndexTableName, condition); }

    public String getDeckIndexTableName() { return deckIndexTableName; }

}
