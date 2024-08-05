package com.task09.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task09.imports.Import;
import lombok.Data;

@DynamoDBTable(tableName = Import.TABLE_FULL_NAME)
@Data
public class WeatherItem {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "forecast")
    private WeatherForecast forecast;
}
