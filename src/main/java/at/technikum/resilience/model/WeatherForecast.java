package at.technikum.resilience.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherForecast(
        double latitude,
        double longitude,
        @JsonProperty("generationtime_ms") double generationTimeMilliseconds,
        @JsonProperty("utc_offset_seconds") int utcOffsetSeconds,
        String timezone,
        @JsonProperty("timezone_abbreviation") String timezoneAbbreviation,
        int elevation,
        @JsonProperty("current_units") CurrentUnits currentUnits,
        CurrentValues current,
        @JsonProperty("daily_units") DailyUnits dailyUnits,
        DailyValues daily
) {

    public record CurrentUnits(
            String time,
            String interval,
            @JsonProperty("temperature_2m") String temperatureTwoMeters,
            @JsonProperty("relative_humidity_2m") String relativeHumidityTwoMeters,
            @JsonProperty("apparent_temperature") String apparentTemperature,
            @JsonProperty("is_day") String isDay,
            String precipitation,
            @JsonProperty("weather_code") String weatherCode,
            @JsonProperty("cloud_cover") String cloudCover,
            @JsonProperty("wind_speed_10m") String windSpeedTenMeters,
            @JsonProperty("wind_direction_10m") String windDirectionTenMeters
    ) {
    }

    public record CurrentValues(
            String time,
            int interval,
            @JsonProperty("temperature_2m") double temperatureTwoMeters,
            @JsonProperty("relative_humidity_2m") int relativeHumidityTwoMeters,
            @JsonProperty("apparent_temperature") double apparentTemperature,
            @JsonProperty("is_day") int isDay,
            int precipitation,
            @JsonProperty("weather_code") int weatherCode,
            @JsonProperty("cloud_cover") int cloudCover,
            @JsonProperty("wind_speed_10m") double windSpeedTenMeters,
            @JsonProperty("wind_direction_10m") int windDirectionTenMeters
    ) {
    }

    public record DailyUnits(
            String time,
            @JsonProperty("weather_code") String weatherCode,
            @JsonProperty("temperature_2m_max") String temperatureTwoMetersMax,
            @JsonProperty("temperature_2m_min") String temperatureTwoMetersMin,
            @JsonProperty("apparent_temperature_max") String apparentTemperatureMax,
            @JsonProperty("apparent_temperature_min") String apparentTemperatureMin,
            String sunrise,
            String sunset,
            @JsonProperty("uv_index_max") String uvIndexMax,
            @JsonProperty("uv_index_clear_sky_max") String uvIndexClearSkyMax
    ) {
    }

    public record DailyValues(
            List<String> time,
            @JsonProperty("weather_code") List<Integer> weatherCode,
            @JsonProperty("temperature_2m_max") List<Double> temperatureTwoMetersMax,
            @JsonProperty("temperature_2m_min") List<Double> temperatureTwoMetersMin,
            @JsonProperty("apparent_temperature_max") List<Double> apparentTemperatureMax,
            @JsonProperty("apparent_temperature_min") List<Double> apparentTemperatureMin,
            List<String> sunrise,
            List<String> sunset,
            @JsonProperty("uv_index_max") List<Double> uvIndexMax,
            @JsonProperty("uv_index_clear_sky_max") List<Double> uvIndexClearSkyMax
    ) {
    }
}
