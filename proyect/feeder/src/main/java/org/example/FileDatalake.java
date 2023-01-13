package org.example;

import com.google.gson.*;
import org.example.interfaces.Datalake;
import org.example.model.Weather;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FileDatalake implements Datalake {
    private final static String Extension = ".events";
    private final File root;

    public FileDatalake(File root) {
        this.root = root;
    }

    @Override
    public void save(List<Weather> weathers, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(prettyJson(weathers));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public File getTodayFile() throws IOException {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        File todayFile = new File(root + "/" + dateFormat.format(date) + Extension);
        if (!todayFile.exists()) todayFile.createNewFile();
        return todayFile;
    }

    @Override
    public File getYesterdayFile() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat(   "yyyyMMdd");
        File yesterdayFile = new File(root + "/" + dateFormat.format(yesterday) + Extension);
        if (!yesterdayFile.exists()) yesterdayFile.createNewFile();
        return yesterdayFile;
    }

    @Override
    public List<Weather> getOldWeathers(File file) {
        if (read(file) != "") return parse(read(file));
        else return null;
    }
    public static String read(File file) {
        String content = "";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) content += linea;
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static List<Weather> parse(String oldsJson) {
        Gson gson = new Gson();
        List<Weather> olds = new ArrayList<>();
        Weather[] weathers = gson.fromJson(oldsJson, Weather[].class);
        for (Weather weather : weathers) olds.add(weather);
        return olds;
    }

    private String prettyJson(Object obj) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(obj);
    }
}