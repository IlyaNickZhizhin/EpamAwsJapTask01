package com.task10.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.task10.dto.TablesResponse;
import com.task10.imports.Import;
import com.task10.model.Reservation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReservationDao {

    @Getter
    private static final ReservationDao instance = new ReservationDao();
    private final AmazonDynamoDB amazonDynamoDB;
    private DynamoDB dynamoDB;
    private DynamoDBMapper dynamoDBMapper;


    private ReservationDao() {
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    private DynamoDB getDynamoDB() {
        if (dynamoDB == null) {
            dynamoDB = new DynamoDB(amazonDynamoDB);
            return dynamoDB;
        } else return dynamoDB;
    }

    private DynamoDBMapper getDynamoDBMapper() {
        if (dynamoDBMapper == null) {
            dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
            return dynamoDBMapper;
        } else return dynamoDBMapper;
    }

    public List<Reservation> getAllReservations(Context context) {
        context.getLogger().log("getAllReservations");
        com.amazonaws.services.dynamodbv2.document.Table table = getDynamoDB().getTable(Import.Reservations_TABLE_FULL_NAME);
        context.getLogger().log("Reservations table :" + Import.Reservations_TABLE_FULL_NAME + " found");
        Iterator<Item> iterator = table.scan().iterator();
        TablesResponse response = new TablesResponse();
        ArrayList<Reservation> reservations = new ArrayList<>();
        int counter = 0;
        while (iterator.hasNext()) {
            counter++;
            Item item = iterator.next();
            Reservation reservationItem = new Reservation();
            reservationItem.setId(item.getString("id"));
            reservationItem.setDate(item.getString("date"));
            reservationItem.setClientName(item.getString("clientName"));
            reservationItem.setSlotTimeEnd(item.getString("slotTimeEnd"));
            reservationItem.setSlotTimeStart(item.getString("slotTimeStart"));
            reservationItem.setPhoneNumber(item.getString("phoneNumber"));
            reservationItem.setTableNumber(item.getInt("tableNumber"));
            reservations.add(reservationItem);
        }
        context.getLogger().log((String.format("Reservations: %s contains %s reservations", Import.Reservations_TABLE_FULL_NAME , counter)));
        return reservations;
    }

    public String createReservation(Context context, Reservation reservation) {
        context.getLogger().log("createReservation - in DAO");
        getDynamoDBMapper().save(reservation);
        context.getLogger().log("Reservation:" + reservation + "saved to dynamoDB");
        return reservation.getId();
    }


}
