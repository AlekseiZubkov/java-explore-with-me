package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {

    List<EventShortDto> getAllEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getAllRequestsOfEventByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateAllRequestsOfEventByUser(
            Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

}