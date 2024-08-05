package com.task09.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

import java.util.List;

@DynamoDBDocument
@Data
public class HourlyForecast {
    private List<Double> temperature_2m;
    private List<String> time;
}
