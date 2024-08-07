package com.task11.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task11.dao.TableDao;
import com.task11.dto.TableCreateRequest;
import com.task11.dto.TableCreateResponse;
import com.task11.dto.TableDetailsResponse;
import com.task11.dto.TablesResponse;
import com.task11.model.Table;
import lombok.Getter;

import java.util.List;

public class TableService extends LambdaService {

    @Getter
    private static final TableService instance = new TableService();
    private final TableDao tableDao = TableDao.getInstance();

    public APIGatewayProxyResponseEvent getAllTables(Context context){
        context.getLogger().log("getAllTables in service");
        try {
            TablesResponse response = mapper.tablesToTablesResponse(getListTables(context));
            context.getLogger().log("Tables founded" + gson.toJson(response));
            return successResponse(response);
        } catch (Exception e) {
            context.getLogger().log("getAllTables failed" + e.getMessage());
            return failedResponse("There was an error in the request" + e.getMessage());
        }
    }

    public APIGatewayProxyResponseEvent getTable(int tableId, Context context){
        context.getLogger().log("getTable in service");
        try {
            TableDetailsResponse table =  mapper.tableDetailsResponseToTableDto(tableDao.getTableById(context, tableId));
            context.getLogger().log("Table founded:" + table);
            return successResponse(table);
        } catch (Exception e) {
            context.getLogger().log("getTable error:" + e.getMessage());
            context.getLogger().log("createTable error: " + e);
            return failedResponse("There was an error in the request" + e.getMessage());
        }
    }

    public APIGatewayProxyResponseEvent createTable(TableCreateRequest request, Context context) {
        try {
            context.getLogger().log("createTable in Service");
            TableCreateResponse response = new TableCreateResponse();
            response.setId(Integer.parseInt(tableDao.createTable(context, mapper.TableCreateRequestToTable(request))));
            context.getLogger().log("createTable response: " + response);
            return successResponse(response);
        } catch (Exception e) {
            context.getLogger().log("createTable error: " + e);
            return failedResponse("There was an error in the request" + e.getMessage());
        }
    }

    List<Table> getListTables(Context context){
        return tableDao.getAllTables(context);
    }

}
