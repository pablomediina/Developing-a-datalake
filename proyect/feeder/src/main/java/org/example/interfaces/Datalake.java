package org.example.interfaces;

import org.example.model.Weather;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Datalake {

    void save(List<Weather> weathersArea, File file);

    File getTodayFile() throws IOException;

    File getYesterdayFile() throws IOException;

    List<Weather> getOldWeathers(File file);
}