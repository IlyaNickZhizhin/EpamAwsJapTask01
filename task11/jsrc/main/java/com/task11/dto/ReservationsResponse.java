package com.task11.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReservationsResponse {
    private List<ReservationDto> reservations;
}