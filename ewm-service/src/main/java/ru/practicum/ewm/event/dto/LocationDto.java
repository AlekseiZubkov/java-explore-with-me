package ru.practicum.ewm.event.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LocationDto {

    private Long id;

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;

}