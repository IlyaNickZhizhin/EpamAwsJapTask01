package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import in.zhizh.WeatherRequest;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
    lambdaName = "weather_client",
	roleName = "weather_client-role",
	isPublishVersion = true,
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaLayer(layerName="weather_api",
		libraries="../task08Layer/target/task08Layer-1.0-SNAPSHOT.jar")
@LambdaUrlConfig
public class WeatherClient implements RequestHandler<Object, String> {

	public String handleRequest(Object request, Context context) {
		return new WeatherRequest().request(context);
	}
}
