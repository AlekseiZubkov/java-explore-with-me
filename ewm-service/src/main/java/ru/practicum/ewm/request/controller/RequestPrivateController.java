package ru.practicum.ewm.request.controller;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestPrivateService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateController {

    private final RequestPrivateService privateService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getAllRequestsByUser(
            @PathVariable Long userId) {
        List<ParticipationRequestDto> requestDtos = privateService.getAllRequestsByUser(userId);
        log.info("Получен список заявок текущего пользователя на участие в чужих событиях, userId = {}.", userId);
        return ResponseEntity.ok().body(requestDtos);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> saveRequest(
            @PathVariable Long userId, @RequestParam Long eventId) {
        ParticipationRequestDto participationRequestDto = privateService.saveRequest(userId, eventId);
        log.info("Добавлен новый запрос на участие в событии, userId = {}, eventId = {}: {}",
                userId, eventId, participationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(participationRequestDto);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateRequest(
            @PathVariable Long userId, @PathVariable Long requestId) {
        ParticipationRequestDto participationRequestDto = privateService.updateRequest(userId, requestId);
        log.info("Отменен свой запрос на участие в событии с id = {} для userId = {}: {}.",
                requestId, userId, participationRequestDto);
        return ResponseEntity.ok(participationRequestDto);
    }

}