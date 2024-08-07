package com.task10;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.task10.controller.Controller;

@LambdaHandler(
    lambdaName = "api_handler",
	roleName = "api_handler-role",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final Controller controller = Controller.getInstance();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		context.getLogger().log("APIGatewayProxyRequestEvent with pass : " + request.getPath());

		if (request.getPath().contains("/tables/")
				&& (request.getResource().split("/").length > 2)) {
			context.getLogger().log("table #" + request.getPath().split("/")[2]);
			return controller.tablesGET(Integer.parseInt(request.getPath().split("/")[2]), context);
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
}
