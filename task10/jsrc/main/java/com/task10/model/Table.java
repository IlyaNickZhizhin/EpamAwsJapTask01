package com.task10.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task10.imports.Import;
import lombok.Setter;

@DynamoDBTable(tableName = Import.Tables_TABLE_FULL_NAME)
@Setter
public class Table {
    private String id;
    private int number;
    private int places;
    private boolean isVip;
    private int minOrder;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "number")
    public int getNumber() {
        return number;
    }

    @DynamoDBAttribute(attributeName = "places")
    public int getPlaces() {
        return places;
    }

    @DynamoDBAttribute(attributeName = "isVip")
    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    @DynamoDBAttribute(attributeName = "minOrder")
    public int getMinOrder() {
        return minOrder;
    }

}
