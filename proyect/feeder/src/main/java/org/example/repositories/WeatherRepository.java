package org.example.repositories;

import org.example.model.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WeatherRepository {
	private List<Weather> weathers;

	public WeatherRepository() {
		weathers = new ArrayList<>();
	}

	public Stream<Weather> all() {
		return weathers.stream();
	}

	public WeatherRepository addAll(List<Weather> weathers) {
		this.weathers.addAll(weathers);
		return this;
	}
}
