package com.task10;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.task10.controller.Controller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import software.amazon.awssdk.services.cognitoidentityprovider.endpoints.internal.Value;

@LambdaHandler(
    lambdaName = "api_handler",
	roleName = "api_handler-role",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private final Controller controller = Controller.getInstance();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		context.getLogger().log("APIGatewayProxyRequestEvent with pass" + request.getPath());

		if (request.getResource().contains("tables")
				&& (request.getResource().split("/").length > 1)) {
			return controller.tablesGET(Integer.parseInt(request.getResource().split("/")[1]), context);
		}
		switch (request.getResource()) {
			case "/signup" : return controller.signupPOST(request.getBody(), context);
			case "/signin" : return controller.signinPOST(request.getBody(), context);
			case "/tables" :
				switch (request.getHttpMethod()) {
					case "GET": return controller.tablesGET(-1, context);
					case "POST": return controller.tablesPOST(request.getBody(), context);
					default: return controller.handleException(request.getPath(), context);
				}
			case "/reservations" :
				switch (request.getHttpMethod()) {
					case "GET": return controller.reservationsGET(request.getBody(), context);
					case "POST": return controller.reservationsPOST(request.getBody(), context);
					default: return controller.handleException(request.getBody(), context);
				}
			default: return controller.handleException(request.getBody(), context);
		}
	}



	private int parseId(APIGatewayProxyRequestEvent request) {
		return Integer.parseInt(request.getPathParameters().getOrDefault("tableId", "-1"));
	}
}
