package com.task11.mapper;

import com.task11.dto.ReservationCreateRequest;
import com.task11.dto.ReservationDto;
import com.task11.dto.ReservationsResponse;
import com.task11.dto.TableCreateRequest;
import com.task11.dto.TableDetailsResponse;
import com.task11.dto.TableDto;
import com.task11.dto.TablesResponse;
import com.task11.model.Reservation;
import com.task11.model.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DtoMapper {

    @Getter
    private static final DtoMapper INSTANCE = new DtoMapper();

    private DtoMapper() {}

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
