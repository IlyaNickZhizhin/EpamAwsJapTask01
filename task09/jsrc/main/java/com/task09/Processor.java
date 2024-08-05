package com.task09;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.task09.imports.Import;
import com.task09.model.WeatherForecast;
import com.task09.model.WeatherItem;
import com.task09.weahter.WeatherRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@LambdaHandler(
    lambdaName = "processor",
	roleName = "processor-role"
)
@LambdaUrlConfig
@DependsOn(name = Import.TABLE_NAME, resourceType = ResourceType.DYNAMODB_TABLE)
public class Processor implements RequestHandler<Object, Map<String, Object>> {

	private LambdaLogger log;
	private Gson gson;
	private DynamoDBMapper dynamoDB;

	private void init(Context context){
		log = context.getLogger();
		gson = new GsonBuilder().setPrettyPrinting().create();
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
		dynamoDB = new DynamoDBMapper(client);
	}


	public Map<String, Object> handleRequest(Object request, Context context) {
		init(context);
		WeatherForecast forecast = gson.fromJson(new WeatherRequest().request(context), WeatherForecast.class);
		log.log("FORECAST: " + forecast);
		WeatherItem item = new WeatherItem();
		item.setId(UUID.randomUUID().toString());
		item.setForecast(forecast);
		log.log("ITEM: " + item);
		dynamoDB.save(item);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("statusCode", 200);
		resultMap.put("body", "finished");
		return resultMap;
	}
}
