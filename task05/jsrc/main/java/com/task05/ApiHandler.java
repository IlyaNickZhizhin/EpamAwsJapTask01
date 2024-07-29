package com.task05;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;

import java.time.LocalDateTime;
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
public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
	private final DynamoDB dynamoDB = new DynamoDB(client);
	private final Table table = dynamoDB.getTable("Events");

	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		try {
			RequestEvent requestEvent = objectMapper.readValue(request.getBody(), RequestEvent.class);
			Event event = new Event();
			event.principalId= requestEvent.principalId;
			event.body=requestEvent.content;
			event.createdAt = LocalDateTime.now();
			event.id = UUID.randomUUID().toString();
			Item item = new Item().withPrimaryKey("id", event.id)
					.withString("principalId", event.principalId)
					.withString("createdAt", event.createdAt.toString())
					.withMap("body", event.body);
			table.putItem(item);
			APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
			response.setStatusCode(201);
			response.setBody(objectMapper.writeValueAsString(event));
			return response;
        } catch (JsonProcessingException e) {
			return new APIGatewayProxyResponseEvent()
					.withStatusCode(500)
					.withBody("Error while processing request" + e.getMessage());
		}
    }

	private class Event{
		private String id;
		private String principalId;
		private LocalDateTime createdAt;
		private Map<String, String> body;
	}

	private class RequestEvent{
		private String principalId;
		private Map<String, String> content;
	}
}
