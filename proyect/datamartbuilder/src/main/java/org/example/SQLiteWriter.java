package org.example;

import org.example.interfaces.DatabaseWriter;
import org.example.model.Weather;

import java.sql.*;
import java.util.List;

public class SQLiteWriter implements DatabaseWriter {
    SQLiteConnection cc = new SQLiteConnection();
    java.sql.Connection cn = cc.connect();
    Statement statement;


    {
        try {
            statement = cn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTableMax() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS maximumTemperatures (" +
                "date TEXT, " +
                "time TEXT, " +
                "place TEXT, " +
                "station TEXT, " +
                "value REAL);"
        );
    }

    @Override
    public void createTableMin() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS minimumTemperatures (" +
                "date TEXT, " +
                "time TEXT, " +
                "place TEXT, " +
                "station TEXT, " +
                "value REAL);"
        );
    }

    @Override
    public void storeMax(Weather weather) throws SQLException {
        PreparedStatement ps = cn.prepareStatement("INSERT INTO maximumTemperatures VALUES(?, ?, ?, ?, ?);");
        storeResolver(weather, ps);
    }

    @Override
    public void storeMin(Weather weather) throws SQLException {
        PreparedStatement ps = cn.prepareStatement("INSERT INTO minimumTemperatures VALUES(?, ?, ?, ?, ?);");
        storeResolver(weather, ps);
    }

    @Override
    public void editMax(Weather weather) throws SQLException {
        PreparedStatement ps = cn.prepareStatement("UPDATE maximumTemperatures SET time = ?, place = ?, station = ?, value = ? WHERE date = ?");
        editResolver(weather, ps);
    }

    @Override
    public void editMin(Weather weather) throws SQLException {
        PreparedStatement ps = cn.prepareStatement("UPDATE minimumTemperatures SET time = ?, place = ?, station = ?, value = ? WHERE date = ?");
        editResolver(weather, ps);
    }

    private void storeResolver(Weather weather, PreparedStatement ps) throws SQLException {
        ps.setString(1, weather.getDate());
        ps.setString(2, weather.getTime());
        ps.setString(3, weather.getUbi());
        ps.setString(4, weather.getIdema());
        ps.setDouble(5, weather.getTa());
        ps.executeUpdate();
    }

    private void editResolver(Weather weather, PreparedStatement ps) throws SQLException {
        ps.setString(1, weather.getTime());
        ps.setString(2, weather.getUbi());
        ps.setString(3, weather.getIdema());
        ps.setDouble(4, weather.getTa());
        ps.setString(5, weather.getDate());
        ps.executeUpdate();
    }

    public void updateDatamart(List<List<Weather>> maxAndMins, List<String> alreadyIn) throws SQLException {
        if (maxAndMins != null) {
            for (List<Weather> maxAndMin : maxAndMins) {
                if (alreadyIn.contains(maxAndMin.get(0).getDate())) {
                    editMax(maxAndMin.get(0));
                    editMin(maxAndMin.get(1));
                } else {
                    storeMax(maxAndMin.get(0));
                    storeMin(maxAndMin.get(1));
                }
            }
        }
    }
}