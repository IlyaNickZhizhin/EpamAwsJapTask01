package com.task06;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.events.DynamoDbTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.model.RetentionSetting;
import com.task06.imports.Import;
import com.task06.mapper.DynamoToPojoMapper;
import com.task06.model.AuditEntity;
import com.task06.model.AuditInsertEntity;
import com.task06.model.AuditUpdateEntity;
import com.task06.model.ConfigurationEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@LambdaHandler(lambdaName = "audit_producer",
	roleName = "audit_producer-role",
	isPublishVersion = false,
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@DynamoDbTriggerEventSource(targetTable = Import.CONFIGURATION_TABLE_NAME, batchSize = 10)
@DependsOn(name = Import.CONFIGURATION_TABLE_NAME, resourceType = ResourceType.DYNAMODB_TABLE)
public class AuditProducer implements RequestHandler<DynamodbEvent, Map<String, Object>> {

	private LambdaLogger log;
	private Gson gson;
	private DynamoDBMapper dynamoDB;
	private DynamoToPojoMapper mapper;

	private final int STATUS_OK = 200;
	private final int STATUS_NOT_FOUND = 404;

	private void init(Context context){
		log = context.getLogger();
		gson = new GsonBuilder().setPrettyPrinting().create();
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
		dynamoDB = new DynamoDBMapper(client);
		mapper = new DynamoToPojoMapper(log);
	}

	public Map<String, Object> handleRequest(DynamodbEvent request, Context context) {
		init(context);
		DynamodbEvent.DynamodbStreamRecord record = null;
		AuditEntity entity = null;
		try {
			record = getUpdate(request);
			log.log(gson.toJson(record));
		} catch (Exception e) {
			return Map.of("statusCode", STATUS_NOT_FOUND, "message", "Audit table was not updated, nothing fouded");
		}
		log.log(record.getEventName());
		if (record.getEventName().equals("INSERT")) {
			entity = saveInsertEvent(record);
		} else if (record.getEventName().equals("MODIFY")) {
			entity = saveUpdateEvent(record);
		} else return Map.of("statusCode", STATUS_OK, "message", "Configuration table was not updated");

		log.log("Entity to SAVE" + gson.toJson(entity));
		dynamoDB.save(entity);

		return Map.of("statusCode", STATUS_OK, "message",
				"Audit table updated successfully, item in configuration was" + record.getEventName());
	}

	private DynamodbEvent.DynamodbStreamRecord getUpdate(DynamodbEvent request) throws Exception {
		return request.getRecords().stream().findFirst().orElseThrow(Exception::new);
	}

	private AuditEntity saveInsertEvent(DynamodbEvent.DynamodbStreamRecord record) {
		Map<String, AttributeValue> image = record.getDynamodb().getNewImage();
		log.log(gson.toJson(image));
		AuditInsertEntity entity = new AuditInsertEntity();
		prepareEntity(image, entity);
		log.log("prepared entity: " + gson.toJson(entity));
		entity.setNewValue(mapper.dynamoToConfigurationEntity(gson.toJson(image)));
		log.log("fulfilled entity: " + gson.toJson(entity));
		return entity;
	}

	private AuditEntity saveUpdateEvent(DynamodbEvent.DynamodbStreamRecord record) {
		Map<String, AttributeValue> image = record.getDynamodb().getNewImage();
		Map<String, AttributeValue> oldImage = record.getDynamodb().getOldImage();
		log.log(gson.toJson(image));
		AuditUpdateEntity entity = new AuditUpdateEntity();
		prepareEntity(image, entity);
		log.log("prepared entity: " + gson.toJson(entity));
		entity.setUpdatedAttribute("value");
		entity.setOldValue(Integer.valueOf(oldImage.get("value").getN()));
		entity.setNewValue(Integer.valueOf(image.get("value").getN()));
		log.log("fulfilled entity: " + gson.toJson(entity));
		return entity;
	}

	private void prepareEntity(Map<String, AttributeValue> image, AuditEntity entity) {
		entity.setId(UUID.randomUUID().toString());
		entity.setItemKey(image.get("key").getS());
		entity.setModificationTime(LocalDateTime.now().toString());
	}

}
