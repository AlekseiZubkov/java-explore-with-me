package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.SortSearch;
import ru.practicum.ewm.event.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static constant.Constants.PATTERN_FOR_DATETIME;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {

    private final EventPublicService publicService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllEvents(
            @RequestParam(defaultValue = "") String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(defaultValue = "VIEWS") SortSearch sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) {

        List<EventShortDto> eventDtos = publicService.getAllEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
        log.info("Получен список событий с возможностью фильтрации, text = {}, categories = {}, paid = {}, " +
                        "rangeStart = {}, rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}.",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return ResponseEntity.ok().body(eventDtos);
    }

    @GetMapping("/{id}")
    /**
     * Получение подробной информации об опубликованном событии по его идентификатору.
     */
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long id, HttpServletRequest request) {
        EventFullDto eventFullDto = publicService.getPublicEventById(id, request);
        log.info("Получено событие, добавленное текущим пользователем, с id = {}: {}.",
                id, eventFullDto);
        return ResponseEntity.ok(eventFullDto);
    }

}