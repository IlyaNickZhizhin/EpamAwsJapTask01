package com.task10.dao;

import com.amazonaws.services.lambda.runtime.Context;
import com.task10.dto.SignInRequest;
import com.task10.dto.SignUpRequest;
import com.task10.imports.Import;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.endpoints.internal.Value;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientDescription;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolDescriptionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CognitoDao {

    private static final Log log = LogFactory.getLog(CognitoDao.class);
    private String userPoolId;
    private String clientId;
    private final CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();;
    @Getter
    private static final CognitoDao INSTANCE = new CognitoDao();

    private String getUserPoolId(Context context) {
        if (userPoolId == null) {
            ListUserPoolsRequest listUserPoolsRequest = ListUserPoolsRequest.builder().build();
            ListUserPoolsResponse userPoolsResponse = cognitoClient.listUserPools(listUserPoolsRequest);
            userPoolId = userPoolsResponse.userPools().stream().filter(p -> p.name().equals(Import.COGNITO_USER_POOL_FULL_NAME)).findFirst().get().id();
            return userPoolId;
        } else return userPoolId;
    }

    private String getClientId(Context context) {
        if (clientId == null) {
            ListUserPoolClientsRequest userPoolClientsRequest = ListUserPoolClientsRequest.builder().userPoolId(getUserPoolId(context)).build();
            ListUserPoolClientsResponse userPoolClientsResponse = cognitoClient.listUserPoolClients(userPoolClientsRequest);
            this.clientId = userPoolClientsResponse.userPoolClients().get(0).clientId();
            context.getLogger().log("ClientId of user pool #" + clientId);
            return clientId;
        } else return clientId;
    }

    private CognitoDao() {
    }

    public AdminInitiateAuthResponse cognitoSignIn(SignInRequest signInRequest, Context context) {
        Map<String, String> authParams = Map.of(
                "USERNAME", signInRequest.getEmail(),
                "PASSWORD", signInRequest.getPassword()
        );
        return cognitoClient.adminInitiateAuth(
                AdminInitiateAuthRequest.builder()
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .authParameters(authParams)
                .userPoolId(getUserPoolId(context))
                .clientId(getClientId(context))
                .build());
    }

    public void cognitoSignUp(SignUpRequest request, Context context) {

        AttributeType userAttrs = AttributeType.builder()
                .name("first_name").value(request.getFirstName())
                .name("last_name").value(request.getLastName())
                .name("email").value(request.getEmail())
                .build();
        List<AttributeType> userAttrsList = new ArrayList<>();
        userAttrsList.add(userAttrs);
        context.getLogger().log("Cognito user pool ID: " + getUserPoolId(context));
        context.getLogger().log("Cognito user client ID: " + getClientId(context));
        try {
            software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest
                    signUpRequest =
                    software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest
                            .builder()
                            .clientId(getClientId(context))
                            .userAttributes(userAttrsList)
                            .username(request.getEmail())
                            .password(request.getPassword()).build();
            cognitoClient.signUp(signUpRequest);
            AdminConfirmSignUpRequest confirmSignUpRequest = AdminConfirmSignUpRequest.builder()
                    .userPoolId(getUserPoolId(context))
                    .username(request.getEmail())
                    .build();
            cognitoClient.adminConfirmSignUp(confirmSignUpRequest);
        } catch (CognitoIdentityProviderException e) {
            throw e;
        }
    }

}
