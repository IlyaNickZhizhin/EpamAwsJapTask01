package com.task11.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task11.dao.CognitoDao;
import com.task11.dto.SignInRequest;
import com.task11.dto.SignInResponse;
import com.task11.dto.SignUpRequest;
import lombok.Getter;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

public class CognitoService extends LambdaService {

    @Getter
    private static final CognitoService instance = new CognitoService();
    private final CognitoDao dao = CognitoDao.getINSTANCE();

    public APIGatewayProxyResponseEvent signup(SignUpRequest request, Context context){
        context.getLogger().log(String.format("SignUp request: %s", request.toString()));
        try {
            dao.cognitoSignUp(request, context);
            context.getLogger().log(String.format("SignUp response: %s", "SUCSESS"));
            return successResponse("");
        } catch (CognitoIdentityProviderException e) {
            context.getLogger().log(String.format("Cognito identity provider exception: %s", e.getMessage()));
            return failedResponse("There was an error in the request." + e.getMessage());
        }
    }
    public APIGatewayProxyResponseEvent signin(SignInRequest request, Context context){
        context.getLogger().log(String.format("SignIn request: %s", request.toString()));
        try {
            AdminInitiateAuthResponse authResponse = dao.cognitoSignIn(request, context);
            context.getLogger().log("SignIn response: SUCCESS");
            SignInResponse response = new SignInResponse();
            context.getLogger().log(String.format("SignIn response: %s", authResponse.toString()));
            response.setAccessToken(authResponse.authenticationResult().idToken());
            return successResponse(response);
        } catch (CognitoIdentityProviderException e) {
            context.getLogger().log(String.format("Cognito identity provider exception: %s", e.getMessage()));
            return failedResponse("There was an error in the request." + e.getMessage());
        }
    }

}
