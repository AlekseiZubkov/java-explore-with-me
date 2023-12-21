package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.StateEvent;
import ru.practicum.ewm.event.service.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static constant.Constants.PATTERN_FOR_DATETIME;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {

    private final EventAdminService adminService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAllEventsByAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<StateEvent> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventFullDto> eventDto = adminService.getAllEventsByAdmin(
                users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("Админ получил список событий users = {}, states = {}, categories = {}, rangeStart = {}, rangeEnd = {}, " +
                "from = {}, size = {}.", users, states, categories, rangeStart, rangeEnd, from, size);
        return ResponseEntity.ok().body(eventDto);
    }

    @PatchMapping("/{eventId}")
    @Validated

    public ResponseEntity<EventFullDto> updateEventByAdmin(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        EventFullDto eventFullDto = adminService.updateEventByAdmin(eventId, updateEventAdminRequest);
        log.info("Событие обновлено админом с id = {}: {}.", eventId, eventFullDto);
        return ResponseEntity.ok(eventFullDto);
    }

}