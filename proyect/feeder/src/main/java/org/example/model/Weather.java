package org.example.model;


public class Weather {
    String idema;
    String ubi;
    String fint;
    double ta;
    Double lat;
    Double lon;


    public String getIdema() {
        return idema;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getUbi() {
        return ubi;
    }

    public String getFint() {
        return fint;
    }

    public double getTa() {
        return ta;
    }

    public String getDate() {
        return getFint().substring(0, 10);
    }

    public String getTime() {
        return getFint().substring(11, 19);
    }

    public void deleteLatLon() {
        this.lat = null;
        this.lon = null;
    }
}