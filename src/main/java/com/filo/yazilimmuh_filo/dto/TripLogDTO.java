package com.filo.yazilimmuh_filo.dto;

import java.time.LocalDate;

public record TripLogDTO(
        LocalDate date,
        int startKm,
        int endKm,
        Long assignmentId
) {

}