package com.task10.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.task10.dto.ReservationCreateRequest;
import com.task10.dto.SignInRequest;
import com.task10.dto.SignUpRequest;
import com.task10.dto.TableCreateRequest;
import com.task10.service.CognitoService;
import com.task10.service.ReservationService;
import com.task10.service.TableService;
import lombok.Getter;

public class Controller {

    @Getter
    private static final Controller instance = new Controller();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    CognitoService cognitoService = CognitoService.getInstance();
    TableService tableService = TableService.getInstance();
    ReservationService reservationService = ReservationService.getInstance();

    public APIGatewayProxyResponseEvent signupPOST(String jsonObject, Context context) {
        context.getLogger().log("signUp POS in controller with " + jsonObject);
        SignUpRequest request = gson.fromJson(jsonObject, SignUpRequest.class);
        return cognitoService.signup(request, context);
    }

    public APIGatewayProxyResponseEvent signinPOST(String jsonObject, Context context) {
        SignInRequest request = gson.fromJson(jsonObject, SignInRequest.class);
        return cognitoService.signin(request, context);
    }

    public APIGatewayProxyResponseEvent tablesPOST(String jsonObject, Context context) {
        TableCreateRequest request = gson.fromJson(jsonObject, TableCreateRequest.class);
        return tableService.createTable(request, context);
    }

    public APIGatewayProxyResponseEvent tablesGET(int tableId, Context context) {
        if (tableId==-1) return tableService.getAllTables(context);
        return tableService.getTable(tableId, context);
    }

    public APIGatewayProxyResponseEvent reservationsPOST(String jsonObject, Context context) {
        ReservationCreateRequest request = gson.fromJson(jsonObject, ReservationCreateRequest.class);
        return reservationService.createReservation(request, context);
    }

    public APIGatewayProxyResponseEvent reservationsGET(String jsonObject, Context context) {
        return reservationService.getAllReservations(context);
    }

    public APIGatewayProxyResponseEvent handleException(String path, Context context) {
        context.getLogger().log(path + " is not handling by this lambda");
        return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody(path + " Not Found");
    }
}
