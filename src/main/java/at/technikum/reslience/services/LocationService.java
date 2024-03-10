package at.technikum.reslience.services;

import at.technikum.reslience.model.GeocodeResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class LocationService {

    private final HttpService httpService;

    private final String apiUrl;

    private final String apiKey;

    public LocationService(
            HttpService httpService,
            @Value("weather.location.api.url") String apiUrl,
            @Value("weather.location.api.key") String apiKey
    ) {
        this.httpService =httpService;
        this.apiUrl =apiUrl;
        this.apiKey= apiKey;
    }

    public GeocodeResponse.Geometry getCoordinatesByCountry(String country) {
        val uriComponents = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .path("json")
                .queryParam("q", country)
                .queryParam("key", apiKey)
                .build();
        return httpService.call(uriComponents.toUriString(), GeocodeResponse.class).results()[0].geometry();
    }
}
