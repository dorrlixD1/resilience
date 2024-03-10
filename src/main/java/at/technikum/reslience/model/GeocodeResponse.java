package at.technikum.reslience.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeocodeResponse(Result[] results) {

    public record Result(Geometry geometry) {
    }

    public record Geometry(
            @JsonProperty("lat") double latitude,
            @JsonProperty("lng") double longitude
    ) {
    }
}




