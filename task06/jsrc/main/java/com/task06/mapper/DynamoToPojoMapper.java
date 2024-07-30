package com.task06.mapper;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.task06.dto.NewValue;


public class DynamoToPojoMapper {


    private final LambdaLogger logger;

    public DynamoToPojoMapper(LambdaLogger logger) {
        this.logger = logger;
    }

    public NewValue dynamoToConfigurationEntity(String fromGsonString) {
        try {
            JsonObject jsonObject = JsonParser.parseString(fromGsonString).getAsJsonObject();
            logger.log(jsonObject.toString());
            NewValue configurationEntity = new NewValue();
            String key = jsonObject.getAsJsonObject("key").get("s").getAsString();
            Integer value = jsonObject.getAsJsonObject("value").get("n").getAsInt();
            configurationEntity.setKey(key);
            configurationEntity.setValue(value);
            logger.log("MAPPED field before send" + configurationEntity.toString());
            return configurationEntity;
        } catch (Exception e) {
            logger.log(e.getMessage());
            NewValue configurationEntity = new NewValue();
            configurationEntity.setKey(fromGsonString);
            configurationEntity.setValue(0);
            return configurationEntity;
        }
    }

}
