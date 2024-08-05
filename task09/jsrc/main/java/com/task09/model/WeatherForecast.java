package com.task09.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

@DynamoDBDocument
@Data
public class WeatherForecast {
    private int elevation;
    private double generationtime_ms;
    private HourlyForecast hourly;
    private HourlyUnits hourly_units;
    private double latitude;
    private double longitude;
    private String timezone;
    private String timezone_abbreviation;
    private int utc_offset_seconds;
}
