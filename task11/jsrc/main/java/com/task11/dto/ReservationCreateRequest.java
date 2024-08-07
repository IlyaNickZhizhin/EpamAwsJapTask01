package com.task11.dto;

import lombok.Data;

@Data
public class ReservationCreateRequest {
    private int tableNumber;
    private String clientName;
    private String phoneNumber;
    private String date;
    private String slotTimeStart;
    private String slotTimeEnd;
}
