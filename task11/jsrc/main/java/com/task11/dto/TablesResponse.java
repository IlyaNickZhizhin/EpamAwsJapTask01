package com.task11.dto;

import lombok.Data;

import java.util.List;

@Data
public class TablesResponse {

    private List<TableDto> tables;
}
