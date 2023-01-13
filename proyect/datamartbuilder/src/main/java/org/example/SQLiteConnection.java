package org.example;


import java.sql.DriverManager;

public class SQLiteConnection {
    java.sql.Connection connect = null;

    public java.sql.Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:datamart.db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return connect;
    }
}