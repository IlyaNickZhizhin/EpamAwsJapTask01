package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import in.zhizh.WeatherRequest;

@LambdaHandler(
    lambdaName = "weather_client",
	roleName = "weather_client-role",
	layers = {"weather_api"}
)
@LambdaLayer(
		layerName = "weather_api",
		libraries = {"lib/task08Layer-1.0-SNAPSHOT.jar"})
@LambdaUrlConfig
public class WeatherClient implements RequestHandler<Object, String> {

	public String handleRequest(Object request, Context context) {
		return new WeatherRequest().request(context);
	}
}
