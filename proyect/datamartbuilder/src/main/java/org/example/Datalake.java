package org.example;

import org.example.model.Weather;
import org.example.repositories.WeatherRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.example.FileDatalake.read;

public class Datalake {
    private WeatherRepository eventRepository;

    public List<List<Weather>> getMaxAndMins(List<File> filesToPost) {
        List<List<Weather>> maxAndMins = new ArrayList<>();
        if (filesToPost.isEmpty()) return null;
        for (File file : filesToPost) {
            List<Weather> maxAndMin = new ArrayList<>();
            List<Weather> weathers = FileDatalake.parse(read(file));
            maxAndMin.add(getMax(weathers));
            maxAndMin.add(getMin(weathers));
            maxAndMins.add(maxAndMin);
        }
        return maxAndMins;
    }

    private Weather getMax(List<Weather> weathers) {
        eventRepository = new WeatherRepository().addAll(weathers);
        return eventRepository.all().max(Comparator.comparing(Weather::getTa)).orElse(null);
    }

    private Weather getMin(List<Weather> weathers) {
        eventRepository = new WeatherRepository().addAll(weathers);
        return eventRepository.all().min(Comparator.comparing(Weather::getTa)).orElse(null);
    }
}