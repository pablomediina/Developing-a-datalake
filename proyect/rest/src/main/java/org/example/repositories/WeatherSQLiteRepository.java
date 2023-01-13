package org.example.repositories;

import org.example.model.WeatherSQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WeatherSQLiteRepository {
	private List<WeatherSQLite> weathers;

	public WeatherSQLiteRepository() {
		weathers = new ArrayList<>();
	}

	public Stream<WeatherSQLite> all() {
		return weathers.stream();
	}

	public WeatherSQLiteRepository addAll(List<WeatherSQLite> weathers) {
		this.weathers.addAll(weathers);
		return this;
	}
}