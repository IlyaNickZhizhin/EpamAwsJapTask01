package com.task10.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.task10.dao.ReservationDao;
import com.task10.dto.ReservationCreateRequest;
import com.task10.dto.ReservationCreateResponse;
import com.task10.dto.ReservationDto;
import com.task10.dto.ReservationsResponse;
import com.task10.mapper.DtoMapper;
import lombok.Getter;

import java.util.stream.Collectors;

public class ReservationService {

    @Getter
    private static final ReservationService instance = new ReservationService();
    private final ReservationDao dao = ReservationDao.getInstance();
    private final DtoMapper mapper = DtoMapper.getINSTANCE();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public APIGatewayProxyResponseEvent createReservation(ReservationCreateRequest request, Context context){
        context.getLogger().log("createReservation - in service");
        try {
            ReservationCreateResponse response = new ReservationCreateResponse();
            response.setReservationId(Integer.parseInt(dao.createReservation(context, mapper.reservationCreateRequestToReservation(request))));
            context.getLogger().log("createReservation done with reservationId " + response.getReservationId());
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(response));
        } catch (Exception e) {
            context.getLogger().log("createReservation done with error " + e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody("There was an error in the request. Possible reasons include invalid input," +
                            " table not found, or conflicting reservations." + e.getMessage());
        }
    };
    public APIGatewayProxyResponseEvent getAllReservations(Context context) {
        context.getLogger().log("getAllReservations - in service");
        try {
            ReservationsResponse response = mapper.reservationToReservationsResponse(dao.getAllReservations(context));
            context.getLogger().log("getAllReservations done with reservations"
                    + response.getReservations().stream()
                    .map(ReservationDto::getId));
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(response));
        } catch (Exception e) {
            context.getLogger().log("getAllReservations with error " + e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400).withBody("There was an error in the request." + e.getMessage());
        }
    };
}
