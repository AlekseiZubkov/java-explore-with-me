package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

import lombok.Data;

@Data
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

}