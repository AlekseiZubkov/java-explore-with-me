package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {

    private final EventPrivateService privateService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllEventsByUser(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventShortDto> eventDtos = privateService.getAllEventsByUser(userId, from, size);
        log.info("Получен список событий, добавленных текущим пользователем, userId = {}, from = {}, size = {}.",
                userId, from, size);
        return ResponseEntity.ok().body(eventDtos);
    }

    @PostMapping
    @Validated
    public ResponseEntity<EventFullDto> saveEvent(
            @PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        EventFullDto eventFullDto = privateService.saveEvent(userId, newEventDto);
        log.info("Добавлено новое событие: {}", eventFullDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventFullDto);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(
            @PathVariable Long userId, @PathVariable Long eventId) {
        EventFullDto eventFullDto = privateService.getEventById(userId, eventId);
        log.info("Получено событие, добавленное текущим пользователем, с id = {} для userId = {}: {}.",
                eventId, userId, eventFullDto);
        return ResponseEntity.ok(eventFullDto);
    }

    @PatchMapping("/{eventId}")
    @Validated
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long userId, @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        EventFullDto eventFullDto = privateService.updateEvent(userId, eventId, updateEventUserRequest);
        log.info("Обновлено событие, добавленное текущим пользователем, с id = {} для userId = {}: {}.",
                userId, eventId, eventFullDto);
        return ResponseEntity.ok(eventFullDto);
    }

    @GetMapping("/{eventId}/requests")

    public ResponseEntity<List<ParticipationRequestDto>> getAllRequestsOfEventByUser(
            @PathVariable Long userId, @PathVariable Long eventId) {
        List<ParticipationRequestDto> requestDtos = privateService.getAllRequestsOfEventByUser(userId, eventId);
        log.info("Получен список запросов на участие в событии текущего пользователя, userId = {}, eventId = {}.",
                userId, eventId);
        return ResponseEntity.ok().body(requestDtos);
    }

    @PatchMapping("/{eventId}/requests")
    @Validated
    public ResponseEntity<EventRequestStatusUpdateResult> updateAllRequestsOfEventByUser(
            @PathVariable Long userId, @PathVariable Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        EventRequestStatusUpdateResult eventRequestStatusUpdateResults = privateService.updateAllRequestsOfEventByUser(userId, eventId, eventRequestStatusUpdateRequest);
        log.info("Изменён статус заявок на участие в событии текущего пользователя, с id = {} для eventId = {}: {}.",
                userId, eventId, eventRequestStatusUpdateResults);
        return ResponseEntity.ok(eventRequestStatusUpdateResults);
    }

}