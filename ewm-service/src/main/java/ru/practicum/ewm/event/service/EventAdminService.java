package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.StateEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    List<EventFullDto> getAllEventsByAdmin(
            List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

}