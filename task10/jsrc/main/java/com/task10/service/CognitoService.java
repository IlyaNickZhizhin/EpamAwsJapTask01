package com.task10.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.task10.dao.CognitoDao;
import com.task10.dto.SignInRequest;
import com.task10.dto.SignInResponse;
import com.task10.dto.SignUpRequest;
import com.task10.mapper.DtoMapper;
import lombok.Getter;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

public class CognitoService {

    @Getter
    private static final CognitoService instance = new CognitoService();
    private final CognitoDao dao = CognitoDao.getINSTANCE();
    private final DtoMapper mapper = DtoMapper.getINSTANCE();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public APIGatewayProxyResponseEvent signup(SignUpRequest request, Context context){
        context.getLogger().log(String.format("SignUp request: %s", request.toString()));
        try {
            dao.cognitoSignUp(request);
            context.getLogger().log(String.format("SignUp response: %s", "SUCSESS"));
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("");
        } catch (CognitoIdentityProviderException e) {
            context.getLogger().log(String.format("Cognito identity provider exception: %s", e.getMessage()));
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("There was an error in the request." + e.getMessage());
        }
    };
    public APIGatewayProxyResponseEvent signin(SignInRequest request, Context context){
        context.getLogger().log(String.format("SignIn request: %s", request.toString()));
        try {
            AdminInitiateAuthResponse authResponse = dao.cognitoSignIn(request);
            context.getLogger().log(String.format("SignIn response: SUCCESS"));
            SignInResponse response = new SignInResponse();
            context.getLogger().log(String.format("SignIn response: %s", authResponse.toString()));
            response.setAccessToken(authResponse.authenticationResult().idToken());
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(response));
        } catch (CognitoIdentityProviderException e) {
            context.getLogger().log(String.format("Cognito identity provider exception: %s", e.getMessage()));
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("There was an error in the request." + e.getMessage());
        }
    };

}
