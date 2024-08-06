package com.task10.dto;

import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.endpoints.internal.Value;

@Data
public class ReservationDto {
    private String id;
    private int tableNumber;
    private String clientName;
    private String phoneNumber;
    private String date;
    private String slotTimeStart;
    private String slotTimeEnd;
}
