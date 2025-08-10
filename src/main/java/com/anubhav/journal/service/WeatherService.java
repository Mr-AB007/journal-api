package com.anubhav.journal.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private final RestTemplate rest = new RestTemplate();

    public Map<String,Object> getGeoCordinate(String city, String country){

        String url = "https://nominatim.openstreetmap.org/search?q=" + city + ","+ country + "&format=json&limit=1";

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "journal-app");
        HttpEntity<Void> reqEntity = new HttpEntity<>(headers);

        ResponseEntity<GeoResult[]> geoResult = rest.exchange(url, HttpMethod.GET,reqEntity,GeoResult[].class);
        WeatherResponse weatherResponse = null;

        if (geoResult.getBody() != null && geoResult.getBody().length > 0) {
            double latitude = Double.parseDouble(geoResult.getBody()[0].lat);
            double longitude = Double.parseDouble(geoResult.getBody()[0].lon);
            weatherResponse = getWeather(latitude, longitude);
        }
        if (weatherResponse == null || weatherResponse.current_weather == null) {
            return Map.of(); // empty map
        }

        Map<String,Object> weather = new HashMap<>();
        weather.put("Location",city.toUpperCase()+","+country.toUpperCase());
        weather.put("Tempreture",weatherResponse.current_weather.temperature);
        weather.put("WinSpeed", weatherResponse.current_weather.windspeed);
        weather.put("WeatherCode",weatherResponse.current_weather.weathercode);

        return weather;

    }

    public WeatherResponse getWeather(double latitude, double longitude){
        String url = "https://api.open-meteo.com/v1/forecast?latitude="+ latitude +"&longitude="+ longitude +"&current_weather=true";

         ResponseEntity<WeatherResponse> resp = rest.getForEntity(url, WeatherResponse.class);
        return resp.getBody();
    }

    public static class WeatherResponse {
        public CurrentWeather current_weather;

        public static class CurrentWeather {
            public double temperature;
            public double windspeed;
            public int weathercode;
        }
    }

    public static class GeoResult {
        public String lat;
        public String lon;
    }
}
