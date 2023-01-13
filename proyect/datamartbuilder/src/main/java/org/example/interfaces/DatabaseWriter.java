package org.example.interfaces;

import org.example.model.Weather;

import java.sql.SQLException;

public interface DatabaseWriter {
    void createTableMax() throws SQLException;

    void createTableMin() throws SQLException;

    void storeMax(Weather weather) throws SQLException;

    void storeMin(Weather weather) throws SQLException;

    void editMax(Weather weather) throws SQLException;

    void editMin(Weather weather) throws SQLException;
}
