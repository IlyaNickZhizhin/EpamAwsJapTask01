package com.task10.dto;

import com.task10.model.Table;
import lombok.Data;

import java.util.List;

@Data
public class TablesResponse {

    private List<Table> tables;
}
