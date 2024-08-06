package com.task10.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task10.imports.Import;
import lombok.Data;

@DynamoDBTable(tableName = Import.Tables_TABLE_FULL_NAME)
@Data
public class Table {
    @DynamoDBHashKey(attributeName = "id")
    private int id;
    private int number;
    private int places;
    private boolean isVip;
    private Integer minOrder;

}
