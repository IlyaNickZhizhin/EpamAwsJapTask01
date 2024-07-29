package com.task05;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;
import com.task05.dto.Event;
import com.task05.dto.RequestEvent;
import com.task05.dto.ResponseEvent;
import com.task05.model.DynamoModel;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@LambdaHandler(lambdaName = "api_handler",
	roleName = "api_handler-role",
	isPublishVersion = false,
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
		authType = AuthType.NONE,
		invokeMode = InvokeMode.BUFFERED
)
@EnvironmentVariables(value = {
		@EnvironmentVariable(key = "region", value = "${region}"),
		@EnvironmentVariable(key = "table", value = "${target_table}")})
@DependsOn(name = "Events", resourceType = ResourceType.DYNAMODB_TABLE)
public class ApiHandler implements RequestHandler<RequestEvent, ResponseEvent> {

	private final Gson gson = new GsonBuilder().create();
	private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
	private final DynamoDBMapper mapper = new DynamoDBMapper(client);

	@Override
	public ResponseEvent handleRequest(RequestEvent request, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log(gson.toJson(request));
		ResponseEvent response = new ResponseEvent();
		response.setStatusCode(200);
		response.setEvent(addDataAndResponse(request, logger));
		return response;
    }

	private Event addDataAndResponse(RequestEvent request, LambdaLogger logger){
		String id = UUID.randomUUID().toString();
		LocalDateTime createdAt = LocalDateTime.now();
		Event event = new Event();
		event.setId(id);
		event.setCreatedAt(createdAt.toString());
		event.setPrincipalId(request.getPrincipalId());
		event.setBody(gson.toJson(request.getContent()));
		logger.log(gson.toJson(event));
		saveEvent(event, request.getContent(), logger);
		return event;
	}

	private Event saveEvent(Event event, Object content, LambdaLogger logger){
		DynamoModel item = new DynamoModel();
		item.setId(event.getId());
		item.setPrincipalId(event.getPrincipalId());
		item.setCreatedAt(event.getCreatedAt());
		Map body;
        try {
            body = new ObjectMapper().readValue(gson.toJson(content), HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        item.setBody(body);
		logger.log(gson.toJson(item));
		mapper.save(item);
		return event;
	}

}
