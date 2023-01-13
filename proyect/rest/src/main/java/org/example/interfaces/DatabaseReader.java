package org.example.interfaces;

import org.example.model.WeatherSQLite;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseReader {
    List<WeatherSQLite> downloadMaxs() throws SQLException;

    List<WeatherSQLite> downloadMins() throws SQLException;
}
