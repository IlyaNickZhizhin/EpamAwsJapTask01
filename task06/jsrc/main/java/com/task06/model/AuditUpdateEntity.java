package com.task06.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Setter;

import static com.task06.imports.Import.AUDIT_TABLE_FULL_NAME;

@Setter
@DynamoDBTable(tableName = AUDIT_TABLE_FULL_NAME)
public class AuditUpdateEntity extends AuditEntity {

    private String updatedAttribute;
    private Integer oldValue;
    private Integer newValue;

    @DynamoDBAttribute(attributeName = "updatedAttribute")
    public String getUpdatedAttribute() {
        return updatedAttribute;
    }
    @DynamoDBAttribute(attributeName = "oldValue")
    public Integer getOldValue() {
        return oldValue;
    }
    @DynamoDBAttribute(attributeName = "newValue")
    public Integer getNewValue() {
        return newValue;
    }
}


/*



   "id": // string, uuidv4
   "itemKey": "CACHE_TTL_SEC",
   "modificationTime": // string, ISO8601 formatted string
   "newValue": {
       "key": "CACHE_TTL_SEC",
       "value": 3600
   },

   {
   "id": // string, uuidv4
   "itemKey": "CACHE_TTL_SEC",
   "modificationTime": // string, ISO8601 formatted string
   "updatedAttribute": "value",
   "oldValue": 3600,
   "newValue": 1800
}
*/