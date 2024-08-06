package com.task10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ReservationVerify {
    int tableNumber;
    LocalTime date;
    LocalTime startTime;
    LocalTime endTime;
}
