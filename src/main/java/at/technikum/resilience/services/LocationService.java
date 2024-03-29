package at.technikum.resilience.services;

import at.technikum.resilience.model.GeocodeResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Service
public class LocationService {

    private final HttpService httpService;

    private final String apiUrl;

    private final String apiKey;

    public LocationService(
            HttpService httpService,
            @Value("${weather.location.api.url}") String apiUrl,
            @Value("${weather.location.api.key}") String apiKey
    ) {
        this.httpService = httpService;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @Retry(name = "httpService")
    @TimeLimiter(name = "httpService")
    @CircuitBreaker(name = "httpService")
    public CompletableFuture<GeocodeResponse.Geometry> getCoordinatesByCountry(String country) {
        val uriComponents = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .path("json")
                .queryParam("q", country)
                .queryParam("key", apiKey)
                .build();
        return CompletableFuture.supplyAsync(() ->
                httpService.call(uriComponents.toUriString(), GeocodeResponse.class)
                        .results()
                        .stream().findFirst().orElseThrow()
                        .geometry());
    }
}
