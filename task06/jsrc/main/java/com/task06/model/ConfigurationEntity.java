package com.task06.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.task06.imports.Import.CONFIGURATION_TABLE_FULL_NAME;

@Setter
@NoArgsConstructor
@DynamoDBTable(tableName = CONFIGURATION_TABLE_FULL_NAME)
@ToString
@DynamoDBDocument
public class ConfigurationEntity {
    String key;
    Integer value;

    @DynamoDBHashKey(attributeName = "key")
    public String getKey() {
        return key;
    }

    @DynamoDBAttribute(attributeName = "value")
    public Integer getValue() {
        return value;
    }

}


/*
    "key": "CACHE_TTL_SEC",
    "value": 3600
*/
