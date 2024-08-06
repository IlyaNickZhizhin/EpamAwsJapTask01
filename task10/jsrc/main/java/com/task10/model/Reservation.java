package com.task10.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task10.imports.Import;
import lombok.Data;

@DynamoDBTable(tableName = Import.Reservations_TABLE_FULL_NAME)
@Data
public class Reservation {

    @DynamoDBHashKey(attributeName = "id")
    private String id;
    private int tableNumber;
    private String clientName;
    private String phoneNumber;
    private String date;
    private String slotTimeStart;
    private String slotTimeEnd;

}
