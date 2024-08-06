package com.task10.dto;

import lombok.Data;

@Data
public class ReservationDto {
    private int id;
    private int tableNumber;
    private String clientName;
    private String phoneNumber;
    private String date;
    private String slotTimeStart;
    private String slotTimeEnd;
}
