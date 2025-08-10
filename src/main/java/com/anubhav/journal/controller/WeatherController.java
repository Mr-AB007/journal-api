package com.anubhav.journal.controller;

import com.anubhav.journal.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String city, @RequestParam String country){

        if (city == null || city.isBlank() || country == null || country.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "City and Country are required"));
        }

        Map<String,Object> result = weatherService.getGeoCordinate(city,country);
        if(result.isEmpty())
                return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }
}
