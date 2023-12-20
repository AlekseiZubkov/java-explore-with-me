package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.service.StatsServerService;

import java.time.LocalDateTime;
import java.util.List;

import static constant.Constants.PATTERN_FOR_DATETIME;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsServerController {

    private final StatsServerService statsServerService;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHit> saveHit(@RequestBody EndpointHit endpointHitDto) {
        endpointHitDto = statsServerService.saveEndpointHit(endpointHitDto);
        log.info("POST EndpointHit {}", endpointHitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getAllStats(
            @RequestParam @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        List<ViewStats> stats = statsServerService.getAllStats(start, end, uris, unique);
        log.info("GET stats start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return ResponseEntity.ok().body(stats);
    }
    @GetMapping("/full-stats")
    public ResponseEntity<List<ViewStats>> getFullStats()
             {
        List<ViewStats> stats = statsServerService.getFullStats();
        log.info("Получить полную статистику ");
        return ResponseEntity.ok().body(stats);
    }

}