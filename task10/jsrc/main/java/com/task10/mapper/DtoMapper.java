package com.task10.mapper;

import com.task10.dto.ReservationCreateRequest;
import com.task10.dto.SignInRequest;
import com.task10.dto.SignUpRequest;
import com.task10.dto.TableCreateRequest;
import com.task10.model.Reservation;
import com.task10.model.Table;
import lombok.Getter;

import java.util.UUID;

public class DtoMapper {

    @Getter
    private static final DtoMapper INSTANCE = new DtoMapper();

    private DtoMapper() {}

    public SignInRequest signUpRequestToSignInRequest(SignUpRequest signUpRequest) {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail(signUpRequest.getEmail());
        signInRequest.setPassword(signUpRequest.getPassword());
        return signInRequest;
    };

    public Table TableCreateRequestToTable(TableCreateRequest tableCreateRequest){
        Table table = new Table();
        table.setId(tableCreateRequest.getId());
        table.setPlaces(tableCreateRequest.getPlaces());
        table.setNumber(tableCreateRequest.getNumber());
        table.setVip(tableCreateRequest.isVip());
        table.setMinOrder(tableCreateRequest.getMinOrder());
        return table;
    };


    public Reservation reservationCreateRequestToReservation(ReservationCreateRequest reservationCreateRequest){
        Reservation reservation = new Reservation();
        reservation.setId(generateUUID());
        reservation.setTableNumber(reservationCreateRequest.getTableNumber());
        reservation.setDate(reservationCreateRequest.getDate());
        reservation.setClientName(reservationCreateRequest.getClientName());
        reservation.setPhoneNumber(reservationCreateRequest.getPhoneNumber());
        reservation.setSlotTimeEnd(reservationCreateRequest.getSlotTimeEnd());
        reservation.setSlotTimeStart(reservationCreateRequest.getSlotTimeStart());
        return reservation;
    };

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
