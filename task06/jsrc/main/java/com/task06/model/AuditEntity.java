package com.task06.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.task06.imports.Import.AUDIT_TABLE_FULL_NAME;

@Setter
@DynamoDBTable(tableName = AUDIT_TABLE_FULL_NAME)
public abstract class AuditEntity {
    private String id;
    private String itemKey;
    private String modificationTime;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "itemKey")
    public String getItemKey() {
        return itemKey;
    }

    @DynamoDBAttribute(attributeName = "modificationTime")
    public String getModificationTime() {
        return modificationTime;
    }
}
