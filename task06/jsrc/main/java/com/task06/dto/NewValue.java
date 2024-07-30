package com.task06.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Setter;
import lombok.ToString;

@DynamoDBDocument
@Setter
@ToString
public class NewValue {

    private String key;
    private Integer value;

    @DynamoDBAttribute(attributeName = "key")
    public String getKey() {
        return key;
    }
    @DynamoDBAttribute(attributeName = "value")
    public Integer getValue() {
        return value;
    }
}