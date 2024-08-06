package com.task10.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.task10.imports.Import;
import com.task10.model.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableDao {

    @Getter
    public static final TableDao instance = new TableDao();
    private final AmazonDynamoDB amazonDynamoDB;
    private DynamoDB dynamoDB;
    private DynamoDBMapper dynamoDBMapper;

    private TableDao() {
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


    public List<Table> getAllTables(Context context) {
        context.getLogger().log("getAllTables");
        com.amazonaws.services.dynamodbv2.document.Table table = getDynamoDB().getTable(Import.Tables_TABLE_FULL_NAME);
        context.getLogger().log("Table: " + Import.Tables_TABLE_FULL_NAME + " found");
        Iterator<Item> iterator = table.scan().iterator();
        ArrayList<Table> tables = new ArrayList<>();
        int counter = 0;
        while (iterator.hasNext()) {
            counter++;
            Item item = iterator.next();
            Table tableItem = new Table();
            tableItem.setId(item.getString("id"));
            tableItem.setNumber(item.getInt("number"));
            tableItem.setPlaces(item.getInt("places"));
            try {
                tableItem.setVip((item.getBoolean("isVip")));
            } catch (Exception e) {
                tableItem.setVip((item.getInt("isVip")) != 0);
            }
            tableItem.setMinOrder(item.getInt("minOrder"));
            tables.add(tableItem);
        }
        context.getLogger().log((String.format("Tables: %s contains %s tables", Import.Tables_TABLE_FULL_NAME , counter)));
        return tables;
    }

    public Table getTableById(Context context, int tableId) {
        context.getLogger().log("getTableById");
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        com.amazonaws.services.dynamodbv2.document.Table tableItem = dynamoDB.getTable(Import.Tables_TABLE_FULL_NAME);
        context.getLogger().log("Table: " + Import.Tables_TABLE_FULL_NAME + " found");
        GetItemSpec getItemSpec = new GetItemSpec().withPrimaryKey("id", String.valueOf(tableId));
        Item item = tableItem.getItem(getItemSpec);
        context.getLogger().log("Table item is" + item);
        Table table = new Table();
        table.setId(item.getString("id"));
        try {
            table.setVip((item.getBoolean("isVip")));
        } catch (Exception e) {
            table.setVip((item.getInt("isVip")) != 0);
        }
        table.setNumber(item.getInt("number"));
        table.setMinOrder(item.getInt("minOrder"));
        table.setPlaces(item.getInt("places"));
        return table;
    }

    public String createTable(Context context, Table table) {
        context.getLogger().log("createTable - in DAO");
        getDynamoDBMapper().save(table);
        context.getLogger().log("Table:" + table + "saved to dynamoDB");
        return table.getId();
    }



}
