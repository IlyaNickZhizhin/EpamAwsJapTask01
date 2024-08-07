package com.task11.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.task11.mapper.DtoMapper;

import java.util.Map;

abstract class LambdaService {

    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    final DtoMapper mapper = DtoMapper.getINSTANCE();

    private final Map<String, String> headers = Map.of(
            "Access-Control-Allow-Headers","Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "*",
            "Accept-Version", "*");

    APIGatewayProxyResponseEvent successResponse(Object dto){
        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(200)
                .withBody(gson.toJson(dto));
    }

    APIGatewayProxyResponseEvent failedResponse(String message){
        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(400)
                .withBody(message);
    }
}
