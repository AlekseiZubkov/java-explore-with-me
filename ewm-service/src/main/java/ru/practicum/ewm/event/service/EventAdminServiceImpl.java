package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.StateActionAdmin;
import ru.practicum.ewm.event.model.StateEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.model.StateRequest;
import ru.practicum.ewm.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static constant.Constants.FORMATTER_FOR_DATETIME;

@Service
@Transactional
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statistic;
    private final EventCategoryService categoryService;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getAllEventsByAdmin(
            List<Long> users, List<StateEvent> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }

        List<Event> events = eventRepository.getAllEventsByAdmin(users, states,
                categories, rangeStart, rangeEnd, page);
        Map<Long, Long> views = returnMapViewStats(events, rangeStart, rangeEnd);
        List<EventFullDto> eventFullDtos = EventMapper.INSTANCE.convertEventListToEventFullDtoList(events);

        eventFullDtos = eventFullDtos.stream()
                .peek(dto -> dto.setConfirmedRequests(
                        requestRepository.countByEventIdAndStatus(dto.getId(), StateRequest.CONFIRMED)))
                .peek(dto -> dto.setViews(views.getOrDefault(dto.getId(), 0L)))
                .collect(Collectors.toList());

        return eventFullDtos;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с ID " + eventId + " не найдено."));
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryService.returnCategory(updateEventAdminRequest.getCategory());
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null &&
                LocalDateTime.now().plusHours(1).isAfter(updateEventAdminRequest.getEventDate())) {
            throw new BadRequestException(String.format("Дата начала изменяемого события должна быть не ранее, " +
                            "чем за час от даты публикации, eventId = %s, updateEventAdminRequest: %s.",
                    eventId, updateEventAdminRequest));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = categoryService.returnOrSaveLocation(updateEventAdminRequest.getLocation());
            event.setLocation(location);
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT) &&
                    !event.getState().equals(StateEvent.PENDING)) {
                throw new ConflictException(String.format("Событие можно публиковать только, если оно в состоянии " +
                                "ожидания публикации, eventId = %s, updateEventAdminRequest: %s.",
                        eventId, updateEventAdminRequest));
            }
            if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.REJECT_EVENT) &&
                    event.getState().equals(StateEvent.PUBLISHED)) {
                throw new ConflictException(String.format("событие можно отклонить только, если оно еще " +
                                "не опубликовано, eventId = %s, updateEventAdminRequest: %s.",
                        eventId, updateEventAdminRequest));
            }

            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(StateEvent.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(StateEvent.CANCELED);
                    break;
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        event = eventRepository.save(event);
        return EventMapper.INSTANCE.toEventFullDto(event);
    }

    private Map<Long, Long> returnMapViewStats(List<Event> events, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());
        List<ViewStats> viewStatList = statistic.getAllStats(rangeStart.format(FORMATTER_FOR_DATETIME),
                rangeEnd.format(FORMATTER_FOR_DATETIME), uris, true);

        Map<Long, Long> views = new HashMap<>();
        for (ViewStats viewStats : viewStatList) {
            Long id = Long.parseLong(viewStats.getUri().split("/events/")[1]);
            views.put(id, views.getOrDefault(id, 0L) + 1);
        }
        return views;
    }

}