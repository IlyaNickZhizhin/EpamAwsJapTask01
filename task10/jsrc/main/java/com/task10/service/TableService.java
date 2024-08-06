package com.task10.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.task10.dao.TableDao;
import com.task10.dto.TableCreateRequest;
import com.task10.dto.TableCreateResponse;
import com.task10.dto.TableDetailsResponse;
import com.task10.dto.TablesResponse;
import com.task10.mapper.DtoMapper;
import lombok.Getter;

public class TableService {

    @Getter
    private static final TableService instance = new TableService();
    private final TableDao  tableDao = TableDao.getInstance();
    private final DtoMapper mapper = DtoMapper.getINSTANCE();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public APIGatewayProxyResponseEvent getAllTables(Context context){
        context.getLogger().log("getAllTables in service");
        try {
            TablesResponse response = new TablesResponse();
            response.setTables(tableDao.getAllTables(context));
            context.getLogger().log("Tables founded" + gson.toJson(response));
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(response));
        } catch (Exception e) {
            context.getLogger().log("getAllTables failed" + e.getMessage());
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400).withBody("There was an error in the request" + e.getMessage());
        }
    }

    public APIGatewayProxyResponseEvent getTable(int tableId, Context context){
        context.getLogger().log("getTable in service");
        try {
            TableDetailsResponse table =  new TableDetailsResponse();
            table.setTable(tableDao.getTableById(context, tableId));
            context.getLogger().log("Table founded:" + table);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(table));
        } catch (Exception e) {
            context.getLogger().log("getTable error:" + e.getMessage());
            context.getLogger().log("createTable error: " + e);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400).withBody("There was an error in the request" + e.getMessage());
        }
    };

    public APIGatewayProxyResponseEvent createTable(TableCreateRequest request, Context context) {
        try {
            context.getLogger().log("createTable in Service");
            TableCreateResponse response = new TableCreateResponse();
            response.setId(Integer.parseInt(tableDao.createTable(context, mapper.TableCreateRequestToTable(request))));
            context.getLogger().log("createTable response: " + response);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(gson.toJson(response));
        } catch (Exception e) {
            context.getLogger().log("createTable error: " + e);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400).withBody("There was an error in the request" + e.getMessage());
        }
    };

}
