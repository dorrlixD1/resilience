package at.technikum.resilience.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeocodeResponse(List<Result> results) {

    public record Result(Geometry geometry) {
    }

    public record Geometry(
            @JsonProperty("lat") double latitude,
            @JsonProperty("lng") double longitude
    ) {
    }
}




