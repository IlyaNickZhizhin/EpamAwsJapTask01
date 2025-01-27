package com.task05;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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
import com.task05.dto.EventDto;
import com.task05.dto.RequestDto;
import com.task05.dto.ResponseDto;
import com.task05.mapper.ObjectContentMapper;
import com.task05.model.DynamoModel;

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
public class ApiHandler implements RequestHandler<RequestDto, ResponseDto> {

	private Gson gson;
	private ObjectContentMapper mapper;
    private DynamoDBMapper dynamoDB;
	private LambdaLogger log;

	private final int STATUS_OK = 201;

	private void init(Context context){
		log = context.getLogger();
		gson = new GsonBuilder().create();
		mapper = new ObjectContentMapper(gson);
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
		dynamoDB = new DynamoDBMapper(client);
	}


	@Override
	public ResponseDto handleRequest(RequestDto request, Context context) {
		init(context);
		log.log(gson.toJson(request));
		return new ResponseDto(STATUS_OK, addDataAndResponse(request));
    }

	private EventDto addDataAndResponse(RequestDto request){
		String id = UUID.randomUUID().toString();
		LocalDateTime createdAt = LocalDateTime.now();
		EventDto event = new EventDto();
		event.setId(id);
		event.setCreatedAt(createdAt.toString());
		event.setPrincipalId(request.getPrincipalId());
		try {
			event.setBody(mapper.objectToContent(request.getContent()));
		} catch (Exception e) {
			log.log("WARNING: Failed to convert request body to event content");
			event.setBody(
					Map.of("error",
							"wrong content provided, expected \"content\" {\"String\" : \"String\" ...}" +
									"but was" + gson.toJson(request.getContent())
					)
			);
		}
		log.log("Event created: " + gson.toJson(event));
		return saveEvent(event);
	}

	private EventDto saveEvent(EventDto event){
		DynamoModel item = new DynamoModel();
		item.setId(event.getId());
		item.setPrincipalId(event.getPrincipalId());
		item.setCreatedAt(event.getCreatedAt());
        item.setBody(event.getBody());
		dynamoDB.save(item);
		log.log("Item saved as: " + gson.toJson(item));
		return event;
	}

}
