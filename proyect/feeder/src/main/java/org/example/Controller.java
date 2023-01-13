package org.example;

import org.example.model.Weather;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Controller {
    Timer timer = new Timer();
    private final AemetWeatherSensor sensor;
    private final FileDatalake datalake;

    public Controller(String apiKey, File datalakeRoot) {
        sensor = new AemetWeatherSensor(apiKey);
        datalake = new FileDatalake(datalakeRoot);
    }

    public void run() {
        long interval = 3600000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    File todayFile = datalake.getTodayFile();
                    File yesterdayFile = datalake.getYesterdayFile();
                    List<Weather> all = sensor.read(Arrays.asList("-16", "-15", "27.5", "28.4"));
                    List<Weather> oldContentYesterday = datalake.getOldWeathers(yesterdayFile);
                    List<Weather> oldContentToday = datalake.getOldWeathers(todayFile);
                    datalake.save(sensor.getYesterdayContent(all, oldContentYesterday), yesterdayFile);
                    datalake.save(sensor.getTodayContent(all, oldContentToday), todayFile);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, interval);
    }
}