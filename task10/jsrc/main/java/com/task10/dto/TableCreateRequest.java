package com.task10.dto;

import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.endpoints.internal.Value;

@Data
public class TableCreateRequest {
    private int id;
    private int number;
    private int places;
    private boolean isVip;
    private Integer minOrder; // Optional field
}
