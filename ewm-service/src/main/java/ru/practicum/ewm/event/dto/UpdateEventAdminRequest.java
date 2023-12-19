package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.model.StateActionAdmin;

import java.time.LocalDateTime;
import javax.validation.constraints.Size;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import static constant.Constants.PATTERN_FOR_DATETIME;

@Data
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionAdmin stateAction; // Изменение состояния события

    @Size(min = 3, max = 120)
    private String title;

}