package com.task04;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.events.SnsEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "sns_handler",
	roleName = "sns_handler-role",
	isPublishVersion = false,
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@SnsEventSource(targetTopic = "lambda_topic")
@DependsOn(name = "lambda_topic", resourceType = ResourceType.SNS_TOPIC)
public class SnsHandler implements RequestHandler<Object, Map<String, Object>> {

	public Map<String, Object> handleRequest(Object request, Context context) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		LambdaLogger logger = context.getLogger();
		logger.log("Context SNS: " + gson.toJson(context));
		logger.log("Request SNS: " + gson.toJson(request));
		return Map.of("statusCode", 200, "lambda", "sns_handler");
	}
}
