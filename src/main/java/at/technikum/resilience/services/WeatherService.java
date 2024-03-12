package at.technikum.resilience.services;

import at.technikum.resilience.model.GeocodeResponse;
import at.technikum.resilience.model.WeatherForecast;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class WeatherService {

    private LocationService locationService;

    private ForecastService forecastService;

    public WeatherForecast getWeatherForecastByCountry(String place) {
        log.info("Loading weather for place '{}'", place);

        GeocodeResponse.Geometry geometry;
        try {
            geometry = locationService.getCoordinatesByCountry(place).get();
            log.info("Received geometry data for '{}' with latitude={} and longitude={}", place, geometry.latitude(), geometry.longitude());
        } catch (Exception e) {
            log.error("Error loading location '{}'", place, e);
            return null;
        }
        try {
            return forecastService.getWeatherForecastByCoordinates(geometry.latitude(), geometry.longitude()).get();
        } catch (Exception e) {
            log.error("Error loading forecast for '{}'", place, e);
        }
        return null;
    }
}
