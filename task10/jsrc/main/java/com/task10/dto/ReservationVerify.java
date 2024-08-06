package com.task10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ReservationVerify {
    int tableNumber;
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
}
