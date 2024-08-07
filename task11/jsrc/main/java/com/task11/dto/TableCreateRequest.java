package com.task11.dto;

import lombok.Data;

@Data
public class TableCreateRequest {
    private int id;
    private int number;
    private int places;
    private boolean isVip;
    private Integer minOrder; // Optional field
}
