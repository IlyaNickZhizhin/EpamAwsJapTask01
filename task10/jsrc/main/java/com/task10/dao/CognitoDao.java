package com.task10.dao;

import com.amazonaws.services.lambda.runtime.Context;
import com.task10.dto.SignInRequest;
import com.task10.dto.SignUpRequest;
import lombok.Getter;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminConfirmSignUpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CognitoDao {

    private String userPoolId;
    private String clientId;
    private final CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();;
    @Getter
    private static final CognitoDao INSTANCE = new CognitoDao();

    private String getUserPoolId() {
        if (userPoolId == null) {
            ListUserPoolsRequest listUserPoolsRequest = ListUserPoolsRequest.builder().build();
            ListUserPoolsResponse userPoolsResponse = cognitoClient.listUserPools(listUserPoolsRequest);
            this.userPoolId = userPoolsResponse.userPools().get(0).id();
            return userPoolId;
        } else return userPoolId;
    }

    private String getClientId() {
        if (clientId == null) {
            ListUserPoolClientsRequest userPoolClientsRequest = ListUserPoolClientsRequest.builder().userPoolId(getUserPoolId()).build();
            ListUserPoolClientsResponse userPoolClientsResponse = cognitoClient.listUserPoolClients(userPoolClientsRequest);
            this.clientId = userPoolClientsResponse.userPoolClients().get(0).clientId();
            return clientId;
        } else return clientId;
    }

    private CognitoDao() {
    }

    public AdminInitiateAuthResponse cognitoSignIn(SignInRequest signInRequest) {
        Map<String, String> authParams = Map.of(
                "USERNAME", signInRequest.getEmail(),
                "PASSWORD", signInRequest.getPassword()
        );
        return cognitoClient.adminInitiateAuth(
                AdminInitiateAuthRequest.builder()
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .authParameters(authParams)
                .userPoolId(getUserPoolId())
                .clientId(getClientId())
                .build());
    }

    public void cognitoSignUp(SignUpRequest request) {

        AttributeType userAttrs = AttributeType.builder()
                .name("name").value(request.getFirstName())
                .name("email").value(request.getEmail())
                .build();

        List<AttributeType> userAttrsList = new ArrayList<>();
        userAttrsList.add(userAttrs);

        try {
            software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest
                    signUpRequest =
                    software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest
                            .builder()
                            .userAttributes(userAttrsList)
                            .username(request.getEmail())
                            .clientId(getClientId())
                            .password(request.getPassword()).build();
            cognitoClient.signUp(signUpRequest);
            AdminConfirmSignUpRequest confirmSignUpRequest = AdminConfirmSignUpRequest.builder()
                    .userPoolId(getUserPoolId())
                    .username(request.getEmail())
                    .build();
            cognitoClient.adminConfirmSignUp(confirmSignUpRequest);
        } catch (CognitoIdentityProviderException e) {
            try {
                AdminConfirmSignUpRequest confirmSignUpRequest = AdminConfirmSignUpRequest.builder()
                        .userPoolId(getUserPoolId())
                        .username(request.getEmail())
                        .build();
                cognitoClient.adminConfirmSignUp(confirmSignUpRequest);
            } catch (CognitoIdentityProviderException e1) {
                throw e;
            }
        }
    }

}
