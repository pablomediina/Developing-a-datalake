package org.example;


import java.io.File;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // args[0] = datalake root
        Controller controller = new Controller(new File(args[0]));
        controller.run();
    }
}