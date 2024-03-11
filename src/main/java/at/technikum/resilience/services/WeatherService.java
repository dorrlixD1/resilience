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

    public WeatherForecast getWeatherForecastByCountry(String country) {
        GeocodeResponse.Geometry geometry;
        try {
            geometry = locationService.getCoordinatesByCountry(country);
        } catch (Exception exc) {
            log.error(exc.getMessage());
            return null;
        }
        try {
            forecastService.getWeatherForecastByCoordinates(geometry.latitude(), geometry.longitude()).get();
        } catch (Exception exc) {
            log.error(exc.getMessage());
        }
        return null;
    }
}
