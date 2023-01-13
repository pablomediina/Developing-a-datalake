package org.example.model;

public class WeatherSQLite {
    String date;
    String time;
    String place;
    String station;
    double value;

    public String getDate() {
        return date;
    }

    public WeatherSQLite(String date, String time, String place, String station, double value) {
        this.date = date;
        this.time = time;
        this.place = place;
        this.station = station;
        this.value = value;
    }
}