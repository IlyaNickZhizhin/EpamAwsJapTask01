package com.task10.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task10.imports.Import;
import lombok.Data;
import lombok.Setter;

@Setter
@DynamoDBTable(tableName = Import.Reservations_TABLE_FULL_NAME)
public class Reservation {
    private String id;
    private int tableNumber;
    private String clientName;
    private String phoneNumber;
    private String date;
    private String slotTimeStart;
    private String slotTimeEnd;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "tableNumber")
    public int getTableNumber() {
        return tableNumber;
    }

    @DynamoDBAttribute(attributeName = "clientName")
    public String getClientName() {
        return clientName;
    }

    @DynamoDBAttribute(attributeName = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @DynamoDBAttribute(attributeName = "date")
    public String getDate() {
        return date;
    }

    @DynamoDBAttribute(attributeName = "slotTimeStart")
    public String getSlotTimeStart() {
        return slotTimeStart;
    }

    @DynamoDBAttribute(attributeName = "slotTimeEnd")
    public String getSlotTimeEnd() {
        return slotTimeEnd;
    }

}
