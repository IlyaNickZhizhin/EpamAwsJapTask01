package com.task10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.utils.Pair;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ReservationVerify {
    int tableNumber;
    LocalTime startTime;
    LocalTime endTime;
}
