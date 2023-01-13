package org.example.interfaces;

import org.example.model.Weather;

import java.io.IOException;
import java.util.List;

public interface WeatherSensor {
    List<Weather> read(List<String> area) throws IOException;

    List<Weather> getTodayContent(List<Weather> all, List<Weather> oldContentToday);

    List<Weather> getYesterdayContent(List<Weather> all, List<Weather> oldContentYesterday);
}