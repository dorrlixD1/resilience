package at.technikum.reslience.services;

import at.technikum.reslience.model.WeatherForecast;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Service
public class ForecastService {

    private static final String[] CURRENT_WEATHER_VARIABLES = {"temperature_2m", "relative_humidity_2m", "apparent_temperature", "is_day", "precipitation", "weather_code", "cloud_cover", "wind_speed_10m", "wind_direction_10m"};
    private static final String[] DAILY_WEATHER_VARIABLES = {"weather_code", "temperature_2m_max", "temperature_2m_min", "apparent_temperature_max", "apparent_temperature_min", "sunrise", "sunset", "uv_index_max", "uv_index_clear_sky_max"};

    private final HttpService httpService;

    private final String apiUrl;

    public ForecastService(
            HttpService httpService,
            @Value("${weather.forecast.api.url}") String apiUrl
    ) {
        this.httpService = httpService;
        this.apiUrl = apiUrl;
    }

    // Retry ( CircuitBreaker ( TimeLimiter ( Function ) ) )
    @Retry(name = "httpService")
    @CircuitBreaker(name = "httpService")
    @TimeLimiter(name = "httpService")
    public CompletableFuture<WeatherForecast> getWeatherForecastByCoordinates(double latitude, double longitude) {
        val uriComponents = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .path("forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current", String.join(",", CURRENT_WEATHER_VARIABLES))
                .queryParam("daily", String.join(",", DAILY_WEATHER_VARIABLES))
                .build();
        return CompletableFuture.supplyAsync(() -> httpService.call(uriComponents.toUriString(), WeatherForecast.class));
    }
}
