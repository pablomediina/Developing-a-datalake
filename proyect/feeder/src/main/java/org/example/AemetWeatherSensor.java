package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.*;
import org.example.interfaces.WeatherSensor;
import org.example.model.Weather;
import org.example.repositories.WeatherRepository;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AemetWeatherSensor implements WeatherSensor {
    private final String url = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";
    private final String apiKey;
    private final Gson gson = new Gson();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private WeatherRepository weatherRepository;

    public AemetWeatherSensor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Weather> read(List<String> area) throws IOException {
        AemetLoaderDatos loader = new AemetLoaderDatos();
        Weather[] allWeathers = gson.fromJson(skipNulls(loader.getData(url, apiKey)), Weather[].class);
        weatherRepository = new WeatherRepository().addAll(List.of(allWeathers));
        List<Weather> weathers = weatherRepository.all()
                .filter(weather -> Double.parseDouble(area.get(0)) < weather.getLon())
                .filter(weather -> weather.getLon() < Double.parseDouble(area.get(1)))
                .filter(weather -> Double.parseDouble(area.get(2)) < weather.getLat())
                .filter(weather -> weather.getLat() < Double.parseDouble(area.get(3)))
                .collect(Collectors.toList());
        for (Weather weather : weathers) weather.deleteLatLon();
        return weathers;
    }

    @Override
    public List<Weather> getTodayContent(List<Weather> all, List<Weather> oldContentToday) {
        if (oldContentToday != null) {
            List<Weather> newsToday = getTodayNew(all, getIdsAndHours(oldContentToday));
            if (!newsToday.isEmpty()) return extractWeathers(addJsons(oldContentToday, newsToday));
            else {
                System.out.println("No new data for today");
                return getToday(all);
            }
        } else return getToday(all);
    }

    @Override
    public List<Weather> getYesterdayContent(List<Weather> all, List<Weather> oldContentYesterday) {
        if (oldContentYesterday != null) {
            List<Weather> newsYesterday = getYesterdayNew(all, getIdsAndHours(oldContentYesterday));
            if (!newsYesterday.isEmpty()) return extractWeathers(addJsons(oldContentYesterday, newsYesterday));
            else {
                System.out.println("No new data from yesterday");
                return oldContentYesterday;
            }
        } else return getYesterday(all);
    }

    private String skipNulls(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(data);
        ArrayNode newRoot = mapper.createArrayNode();
        for (JsonNode element : root) if (element.has("ta")) newRoot.add(element);
        return newRoot.toString();
    }

    private List<Weather> getToday(List<Weather> weathers) {
        weatherRepository = new WeatherRepository().addAll(weathers);
        return weatherRepository.all().filter(this::isOfToday).collect(Collectors.toList());
    }

    private List<Weather> getTodayNew(List<Weather> weathers, List<List<String>> oldIdsAndHours) {
        weatherRepository = new WeatherRepository().addAll(weathers);
        return weatherRepository.all()
                .filter(this::isOfToday)
                .filter(event -> !isAlreadySaved(event, oldIdsAndHours))
                .collect(Collectors.toList());
    }

    private boolean isOfToday(Weather weather) {
        Date date = new Date();
        return weather.getFint().contains(dateFormat.format(date));
    }

    private List<Weather> getYesterday(List<Weather> weathers) {
        weatherRepository = new WeatherRepository().addAll(weathers);
        return weatherRepository.all().filter(this::isOfYesterday).collect(Collectors.toList());
    }

    private List<Weather> getYesterdayNew(List<Weather> weathers, List<List<String>> oldIdsAndHours) {
        weatherRepository = new WeatherRepository().addAll(weathers);
        return weatherRepository.all()
                .filter(this::isOfYesterday)
                .filter(event -> !isAlreadySaved(event, oldIdsAndHours))
                .collect(Collectors.toList());
    }

    private boolean isOfYesterday(Weather weather) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        return weather.getFint().contains(dateFormat.format(yesterday));
    }

    private boolean isAlreadySaved(Weather weather, List<List<String>> oldIdsAndHours) {
        List<String> couple = createCouple(weather);
        return oldIdsAndHours.contains(couple);
    }

    private List<List<String>> getIdsAndHours(List<Weather> weathers) {
        List<List<String>> couples = new ArrayList<>();
        for (Weather weather : weathers) couples.add(createCouple(weather));
        return couples;
    }

    private List<String> createCouple(Weather weather) {
        List<String> couple = new ArrayList<>();
        JSONObject json = new JSONObject(gson.toJson(weather));
        couple.add(json.get("idema").toString());
        couple.add(json.get("fint").toString());
        return couple;
    }

    private List<Weather> extractWeathers(JsonArray jsonArray) {
        List<Weather> weathers = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            Weather weather = gson.fromJson(obj, Weather.class);
            weathers.add(weather);
        }
        return weathers;
    }

    private JsonArray addJsons(List<Weather> oldWeathers, List<Weather> newWeathers) {
        JsonArray array1 = gson.fromJson(new Gson().toJson(oldWeathers), JsonArray.class);
        JsonArray array2 = gson.fromJson(new Gson().toJson(newWeathers), JsonArray.class);
        for (JsonElement jsonElement : array2) array1.add(jsonElement);
        return array1;
    }
}