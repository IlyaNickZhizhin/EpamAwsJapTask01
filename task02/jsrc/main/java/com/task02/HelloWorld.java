package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.Map;

@LambdaHandler(lambdaName = "hello_world",
	roleName = "hello_world-role",
	isPublishVersion = false,
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private final Map<String, String> RESPONSE_HEADERS = Map.of("Content-Type", "application/json");
	private String method;
	private String path;
	private final String messageBody = "{\n    \"statusCode\": %s,\n    \"message\": \"%s\"\n}";
	private final String badRequestMessage = "Bad request syntax or unsupported method. Request path: %s. HTTP method: %s";

	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent request, Context context) {
		method = request.getRequestContext().getHttp().getMethod();
		path = request.getRequestContext().getHttp().getPath();
		return (path.equals("/hello") && method.equals("GET")) ?
				handleOkRequest() : handleOtherRequest();
	}


	private APIGatewayV2HTTPResponse handleOkRequest(){
		int okStatus = 200;
        String okMessage = String.format(messageBody, okStatus, "Hello from Lambda");
        return buildResponse(okStatus, okMessage);
	}

	private APIGatewayV2HTTPResponse handleOtherRequest(){
		String message = String.format(badRequestMessage, path, method);
		int errorStatus = 400;
		String errorMessageTemplate =String
			.format(messageBody, errorStatus, message);
        return buildResponse(errorStatus, errorMessageTemplate);
	}

	private APIGatewayV2HTTPResponse buildResponse(int status, String message){
		return  APIGatewayV2HTTPResponse.builder()
				.withStatusCode(status)
				.withHeaders(RESPONSE_HEADERS)
				.withBody(message)
				.build();
	}

}
