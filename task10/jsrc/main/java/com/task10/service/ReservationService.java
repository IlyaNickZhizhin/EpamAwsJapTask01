package com.task10.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.task10.dao.ReservationDao;
import com.task10.dto.ReservationCreateRequest;
import com.task10.dto.ReservationCreateResponse;
import com.task10.dto.ReservationDto;
import com.task10.dto.ReservationVerify;
import com.task10.dto.ReservationsResponse;
import com.task10.mapper.DtoMapper;
import com.task10.model.Reservation;
import com.task10.model.Table;
import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {

    @Getter
    private static final ReservationService instance = new ReservationService();
    private final ReservationDao dao = ReservationDao.getInstance();
    private final DtoMapper mapper = DtoMapper.getINSTANCE();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final TableService tableService = TableService.getInstance();

    public APIGatewayProxyResponseEvent createReservation(ReservationCreateRequest request, Context context){
        context.getLogger().log("createReservation - in service");
        try {
            isTableExistAndAvailable(request, context);
            ReservationCreateResponse response = new ReservationCreateResponse();
            response.setReservationId(dao.createReservation(context, mapper.reservationCreateRequestToReservation(request)));
            context.getLogger().log("createReservation done with reservationId " + response.getReservationId());
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(response));
        } catch (Exception e) {
            context.getLogger().log("createReservation done with error " + e.getMessage());
            return failedResponse(e.getMessage());
        }
    }
    public APIGatewayProxyResponseEvent getAllReservations(Context context) {
        context.getLogger().log("getAllReservations - in service");
        try {
            ReservationsResponse response = mapper.reservationToReservationsResponse(getReservations(context));
            context.getLogger().log("getAllReservations done with reservations"
                    + response.getReservations().stream()
                    .map(ReservationDto::getId).collect(Collectors.toList()));
            return successResponse(response);
        } catch (Exception e) {
            context.getLogger().log("getAllReservations with error " + e.getMessage());
            return failedResponse(e.getMessage());
        }
    }

    private List<Reservation> getReservations(Context context){
        return dao.getAllReservations(context);
    }

    private APIGatewayProxyResponseEvent successResponse(Object dto){
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(dto));
    }

    private APIGatewayProxyResponseEvent failedResponse(String message){
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(400).withBody("There was an error in the request." + message);
    }

    public void isTableExistAndAvailable(ReservationCreateRequest newRequest, Context context) throws Exception {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
        String reservationExceptionMessage = "There was an error in the request. " +
                "Possible reasons include invalid input, table not found, or conflicting reservations.";
        List<Integer> existingTable = tableService.getListTables(context)
                .stream().map(Table::getNumber).collect(Collectors.toList());
        if (!existingTable.contains(newRequest.getTableNumber())){
            throw new Exception(reservationExceptionMessage);
        }
        List<ReservationVerify> existingReserves = getReservations(context)
                .stream()
                .map(r ->
                        new ReservationVerify(r.getTableNumber(),
                                LocalTime.parse(r.getDate(), DATE_FORMATTER),
                                LocalTime.parse(r.getSlotTimeStart(), TIME_FORMATTER),
                                LocalTime.parse(r.getSlotTimeEnd(), TIME_FORMATTER))
                ).collect(Collectors.toList());
        LocalTime newRequestDate = LocalTime.parse(newRequest.getDate(), DATE_FORMATTER);
        LocalTime newRequestStartTime = LocalTime.parse(newRequest.getSlotTimeStart(), TIME_FORMATTER);
        LocalTime newRequestEndTime = LocalTime.parse(newRequest.getSlotTimeEnd(), TIME_FORMATTER);
        for (ReservationVerify reservation : existingReserves) {
            if (reservation.getTableNumber() == newRequest.getTableNumber()) {
                if (isOverlapping(newRequestDate, newRequestStartTime, newRequestEndTime, reservation.getDate(), reservation.getStartTime(), reservation.getEndTime())) {
                    throw new Exception(reservationExceptionMessage);
                }
            }
        }
    }
    private boolean isOverlapping(LocalTime date1, LocalTime start1, LocalTime end1, LocalTime date2, LocalTime start2, LocalTime end2) {
        return date1.equals(date2) && (start1.isBefore(end2) && start2.isBefore(end1));
    }



}
