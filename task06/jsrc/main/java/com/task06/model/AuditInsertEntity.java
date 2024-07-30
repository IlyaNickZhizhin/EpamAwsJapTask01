package com.task06.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.task06.dto.NewValue;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.task06.imports.Import.AUDIT_TABLE_FULL_NAME;

@Setter
@DynamoDBTable(tableName = AUDIT_TABLE_FULL_NAME)
public class AuditInsertEntity extends AuditEntity {

    private NewValue newValue;

    @DynamoDBAttribute(attributeName = "newValue")
    public NewValue getNewValue() {
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
