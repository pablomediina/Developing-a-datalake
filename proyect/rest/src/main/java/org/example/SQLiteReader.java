package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.interfaces.DatabaseReader;
import org.example.model.WeatherSQLite;
import org.example.repositories.WeatherSQLiteRepository;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class SQLiteReader implements DatabaseReader {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public String getMaxsFromTo(String from, String to) {
        try {
            List<WeatherSQLite> filtered = filter(format.parse(from), format.parse(to), downloadMaxs());
            if (!filtered.isEmpty()) return prettyJson(filtered);
            return "No max temperatures available";
        } catch (ParseException e) {
            return "Format is not met";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMinsFromTo(String from, String to) {
        try {
            List<WeatherSQLite> filtered = filter(format.parse(from), format.parse(to), downloadMins());
            if (!filtered.isEmpty()) return prettyJson(filtered);
            return "No min temperatures available";
        } catch (ParseException e) {
            return "Format is not met";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<WeatherSQLite> downloadMaxs() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:datamart.db");
        Statement st = connection.createStatement();
        ResultSet resultsMaxs = st.executeQuery("SELECT * FROM maximumTemperatures");
        return getObjects(resultsMaxs);
    }

    @Override
    public List<WeatherSQLite> downloadMins() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:datamart.db");
        Statement st = connection.createStatement();
        ResultSet resultsMins = st.executeQuery("SELECT * FROM minimumTemperatures");
        return getObjects(resultsMins);
    }

    private List<WeatherSQLite> getObjects(ResultSet results) throws SQLException {
        List<WeatherSQLite> objs = new ArrayList<>();
        while (results.next()) {
            String date = results.getString("date");
            String time = results.getString("time");
            String place = results.getString("place");
            String station = results.getString("station");
            double value = results.getDouble("value");
            objs.add(new WeatherSQLite(date, time, place, station, value));
        }
        return objs;
    }

    private List<WeatherSQLite> filter(Date from, Date to, List<WeatherSQLite> weathers) {
        WeatherSQLiteRepository eventRepository;
        eventRepository = new WeatherSQLiteRepository().addAll(weathers);
        return eventRepository.all()
                .filter(weather -> isFrom(weather, from))
                .filter(weather -> isTo(weather, to))
                .collect(Collectors.toList());
    }

    private boolean isFrom(WeatherSQLite weatherSQLite, Date from) {
        try {
            String weatherDate = weatherSQLite.getDate().replace("-", "");
            Date weatherFrom = format.parse(weatherDate);
            return from.before(weatherFrom);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isTo(WeatherSQLite weatherSQLite, Date to) {
        try {
            String weatherDate = weatherSQLite.getDate().replace("-", "");
            Date weatherFrom = format.parse(weatherDate);
            return to.after(weatherFrom);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String prettyJson(Object obj) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(obj);
    }
}