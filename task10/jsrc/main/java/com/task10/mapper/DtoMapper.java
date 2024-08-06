package com.task10.mapper;

import com.task10.dto.ReservationCreateRequest;
import com.task10.dto.ReservationDto;
import com.task10.dto.ReservationsResponse;
import com.task10.dto.SignInRequest;
import com.task10.dto.SignUpRequest;
import com.task10.dto.TableCreateRequest;
import com.task10.dto.TableDetailsResponse;
import com.task10.dto.TableDto;
import com.task10.dto.TablesResponse;
import com.task10.model.Reservation;
import com.task10.model.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
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
    }

    public Table TableCreateRequestToTable(TableCreateRequest tableCreateRequest){
        Table table = new Table();
        table.setId( String.valueOf(tableCreateRequest.getId()));
        table.setPlaces(tableCreateRequest.getPlaces());
        table.setNumber(tableCreateRequest.getNumber());
        table.setVip(tableCreateRequest.isVip());
        table.setMinOrder(tableCreateRequest.getMinOrder());
        return table;
    }

    public TablesResponse tablesToTablesResponse(List<Table> tables){
        TablesResponse tablesResponse = new TablesResponse();
        List<TableDto> tableDtos = new ArrayList<>();
        for (Table table : tables) {
            tableDtos.add(tableToTableDto(table));
        }
        tablesResponse.setTables(tableDtos);
        return tablesResponse;
    }

    public TableDto tableToTableDto(Table table){
        TableDto tableDto = new TableDto();
        tableDto.setId(Integer.parseInt(table.getId()));
        tableDto.setPlaces(table.getPlaces());
        tableDto.setNumber(table.getNumber());
        tableDto.setVip(table.isVip());
        tableDto.setMinOrder(table.getMinOrder());
        return tableDto;
    }


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
    }

    public ReservationsResponse reservationToReservationsResponse(List<Reservation> reservations){
        ReservationsResponse reservationsResponse = new ReservationsResponse();
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDtos.add(ReservationToReservationDto(reservation));
        }
        reservationsResponse.setReservations(reservationDtos);
        return reservationsResponse;
    }

    public ReservationDto ReservationToReservationDto(Reservation reservation){
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setTableNumber(reservation.getTableNumber());
        reservationDto.setDate(reservation.getDate());
        reservationDto.setClientName(reservation.getClientName());
        reservationDto.setPhoneNumber(reservation.getPhoneNumber());
        reservationDto.setSlotTimeEnd(reservation.getSlotTimeEnd());
        reservationDto.setSlotTimeStart(reservation.getSlotTimeStart());
        return reservationDto;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public TableDetailsResponse tableDetailsResponseToTableDto(Table table) {
        TableDetailsResponse tableDetailsResponse = new TableDetailsResponse();
        tableDetailsResponse.setId(Integer.parseInt(table.getId()));
        tableDetailsResponse.setPlaces(table.getPlaces());
        tableDetailsResponse.setNumber(table.getNumber());
        tableDetailsResponse.setVip(table.isVip());
        tableDetailsResponse.setMinOrder(table.getMinOrder());
        return tableDetailsResponse;
    }
}
