package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.model.StateStatusRequest;

import java.util.List;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Long> requestIds;

    @NotNull
    private StateStatusRequest status;

}