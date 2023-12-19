package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.SortSearch;

import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface EventPublicService {

    List<EventShortDto> getAllEvents(
            String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, SortSearch sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getPublicEventById(Long eventId, HttpServletRequest request);

}